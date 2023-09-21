from logging import getLogger

import javalang

from preprocessing.CodeFileRepresentation import Attribute, Method, Classifier, Enum_, Parameter, IdentifierString
from preprocessing.Tokenizer import WordTokenizer

log = getLogger(__name__)
ONLY_PUBLIC_METHODS = True
ONLY_PUBLIC_ATTRIBUTES = False
COMMENT_TOKENIZER = WordTokenizer(None)
"""
Helper methods to extract the information of the javalang parser output object (type node)
"""


def extract_type(type_node, file_name):
    if isinstance(type_node, javalang.tree.ClassDeclaration):
        return _extract_class(type_node, file_name)
    elif isinstance(type_node, javalang.tree.InterfaceDeclaration):
        return _extract_interface(type_node, file_name)
    elif isinstance(type_node, javalang.tree.EnumDeclaration):
        return _extract_enum(type_node, file_name)
    elif isinstance(type_node, javalang.tree.AnnotationDeclaration):
        log.info("Extracting annotation declaration called")
        return None
    else:
        log.info("No matching TypeDeclaration-Subclass!")
        return None

    
def _extract_class(class_node, file_name):
    class_name = IdentifierString(file_name, class_node.name)
    implemented_classifiers = []
    extended_classifiers = []
    if class_node.extends:
        extended_classifiers.append(IdentifierString(file_name, class_node.extends.name))
    if class_node.implements:
        for super_classifier in class_node.implements:
            implemented_classifiers.append(IdentifierString(file_name, super_classifier.name))
    
    inner_classifiers = _extract_inner_classifier(class_node, file_name)
    class_object = Classifier(class_name, IdentifierString(file_name, ""), inner_classifiers=inner_classifiers,
                              implemented_classifiers=implemented_classifiers, extended_classifiers=extended_classifiers)

    return _extract_attributes_and_methods_and_doc(class_object, class_node, file_name)


def _extract_interface(interface_node, file_name):
    interface_name = IdentifierString(file_name, interface_node.name)
    extended_classifiers = []
    if interface_node.extends:
        for super_interface in interface_node.extends:
            extended_classifiers.append(IdentifierString(file_name, super_interface.name))
    inner_classifiers = _extract_inner_classifier(interface_node, file_name)
    interface_object = Classifier(interface_name, IdentifierString(file_name, ""), inner_classifiers=inner_classifiers,
                                  extended_classifiers=extended_classifiers)
    
    return _extract_attributes_and_methods_and_doc(interface_object, interface_node, file_name)

        
def _extract_enum(enum_node, file_name):
    enum_name = IdentifierString(file_name, enum_node.name)
    implemented_classifiers = []
    if enum_node.implements:
        for super_interface in enum_node.implements:
            implemented_classifiers.append(IdentifierString(file_name, super_interface.name))
            
    constants = []
    for con in enum_node.body.constants:
        constants.append(IdentifierString(file_name, con.name))

    enum_object = Enum_(enum_name, IdentifierString(file_name, ""), constants=constants, implemented_classifiers=implemented_classifiers)
    return _extract_attributes_and_methods_and_doc(enum_object, enum_node, file_name)


def _extract_inner_classifier(cls_node, file_name):
    inner_classifiers = []
    for body_elem in cls_node.body:
        if isinstance(body_elem, javalang.tree.ClassDeclaration):
            inner_classifiers.append(_extract_class(body_elem, file_name))
        elif isinstance(body_elem, javalang.tree.InterfaceDeclaration):
            inner_classifiers.append(_extract_interface(body_elem, file_name))
        elif isinstance(body_elem, javalang.tree.EnumDeclaration):
            inner_classifiers.append(_extract_enum(body_elem, file_name))
        else:
            continue
    return inner_classifiers

        
def _extract_attributes_and_methods_and_doc(classifier_object, classifier_node, file_name):
    if classifier_node.documentation:
        classifier_object.comment = _extract_comment(classifier_node.documentation, file_name)
    classifier_object.attributes = _extract_attributes(classifier_node.fields, file_name)
    classifier_object.methods = _extract_methods(classifier_node.methods, file_name)
    
    return classifier_object


def _extract_comment(comment: str, file_name):
    return IdentifierString(file_name, *(COMMENT_TOKENIZER.tokenize_to_string_list(comment)))


def _extract_attributes(attribute_nodes, file_name):
    """Return an attribute object list."""
    attr_list = []
    for attr in attribute_nodes:
        if not ONLY_PUBLIC_ATTRIBUTES or attr.modifiers.contains("public"):  # logical Implication
            attr_init_value, left_side_identifier = _extract_children_strings(attr.declarators[0].initializer, file_name)
            attr_obj = Attribute(IdentifierString(file_name, attr.type.name),
                                 IdentifierString(file_name, attr.declarators[0].name), attr_init_value, IdentifierString(file_name, ""))
            if attr.documentation:
                attr_obj.comment = _extract_comment(attr.documentation, file_name)
            attr_list.append(attr_obj)
    return attr_list


def _extract_methods(method_nodes, file_name):
    """Returns a method object list. """
    meth_list = []
    for meth in method_nodes:
        #if not(ONLY_PUBLIC_METHODS) or not("private" in meth.modifiers or "protected" in meth.modifiers):
        if not ONLY_PUBLIC_METHODS or "public" in meth.modifiers:  # logical Implication
            meth_obj = Method(IdentifierString(file_name, ""), IdentifierString(file_name, meth.name), IdentifierString(file_name, ""),
                              IdentifierString(file_name, ""), IdentifierString(file_name, ""))
            if meth.return_type:
                meth_obj.return_type = IdentifierString(file_name, meth.return_type.name)
            if meth.documentation:
                meth_obj.comment = _extract_comment(meth.documentation, file_name)
            meth_obj.set_params(_extract_parameters(meth.parameters, file_name))
            meth_obj.body, meth_obj.left_side_identifiers = _extract_children_strings(meth.body, file_name)
            meth_list.append(meth_obj)
    return meth_list

            
def _extract_parameters(parameter_nodes, file_name):
    """Returns a parameter object list."""
    param_list = []
    for param in parameter_nodes:
        type_suffix = ""
        if len(param.type.dimensions) == param.type.dimensions.count(None):  # it's an array type
            type_suffix = "[]" * len(param.type.dimensions)
        param_list.append(Parameter(IdentifierString(file_name, param.type.name + type_suffix), IdentifierString(file_name, param.name)))
    return param_list

        
def _extract_children_strings(body_node, file_name) -> tuple[IdentifierString, IdentifierString]:
    """Returns an IdentifierString that contains all identifiers in the given body node"""
    
    strings_in_body, left_side_identifiers = _traverse_node(body_node, file_name)
    return IdentifierString(file_name, *strings_in_body), IdentifierString(file_name, *left_side_identifiers)

    
def _traverse_node(node: javalang.ast.Node, file_name) -> [str]:
    strings_in_node, left_side_identifiers = [], []
    if isinstance(node, list) or isinstance(node, set):
        for elem in node:
            child_strings_in_node, child_left_side_identifiers = _traverse_node(elem, file_name)
            left_side_identifiers += child_left_side_identifiers
            strings_in_node += child_strings_in_node
    elif isinstance(node, str):
        if node:
            strings_in_node.append(node)
        else:  # Empty string -> Do nothing
            pass
    elif isinstance(node, javalang.ast.Node):
        for elem in node.children:
            child_strings_in_node, child_left_side_identifiers = _traverse_node(elem, file_name)
            left_side_identifiers += child_left_side_identifiers
            strings_in_node += child_strings_in_node
        a = _parse_left_side_identifiers(node)
        if a is None:
            print("as")
        left_side_identifiers += a
    elif not node:
        pass  # Empty node -> Do nothing
    else:
        log.info(str(node) + "is neither a node nor a string nor a list nor None") 
    
    return strings_in_node, left_side_identifiers


def _parse_left_side_identifiers(node):
    # Returns name of all variable names that are on the left side of an assignment
    if isinstance(node, javalang.tree.LocalVariableDeclaration) or isinstance(node, javalang.tree.VariableDeclaration):
        decl_strings = []
        for decl in node.declarators:
            if isinstance(decl, javalang.tree.VariableDeclarator):
                decl_strings += [decl.name]
            else:
                log.error(f"Unknown case: {type(decl)} is not a VariableDeclaration")
        return decl_strings
    elif isinstance(node, javalang.tree.Assignment):
        if isinstance(node.expressionl, javalang.tree.MemberReference):
            return [node.expressionl.qualifier, node.expressionl.member]
        elif isinstance(node.expressionl, javalang.tree.This) or isinstance(node.expressionl, javalang.tree.Cast):
            if len(node.expressionl.selectors) == 1 and isinstance(node.expressionl.selectors[0], javalang.tree.MemberReference):
                return [node.expressionl.selectors[0].member]
            else:
                log.error(f"Unknown case: {node.expressionl.selectors} has more than 1 selector or is not a MemberReference")
        elif isinstance(node.expressionl, javalang.tree.MethodInvocation):
            strings = []
            for arg in node.expressionl.arguments:
                if isinstance(arg, javalang.tree.MemberReference):
                    strings += [arg.qualifier, arg.member]
            return [node.expressionl.qualifier, node.expressionl.member] + strings
        else:
            log.error(f"Unknown case: {type(node.expressionl)} on the left side of an assignment")
    else:
        return []
