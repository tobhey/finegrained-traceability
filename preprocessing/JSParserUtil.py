from logging import getLogger
import logging
from utility import FileUtil
from preprocessing.CodeFileRepresentation import *
import esprima

log = getLogger(__name__)
logging.basicConfig(level=logging.INFO)
"""
Contains functions to parse the AST returned by the pycparser into a code file representation

"""


def extract_FileAST(fileAST, filepath):
    assert isinstance(fileAST, esprima.nodes.Script) or isinstance(fileAST, esprima.nodes.Module)
    file_name = FileUtil.get_filename_from_path(filepath)
    # Use mock class to hold the methods
    class_name = IdentifierString(file_name, file_name.replace(".jsp", ""))
    super_classifiers = []
    class_object = Classifier(class_name, IdentifierString(file_name, ""))
    attributes = []
    functions = []
    inner_classifiers = []
    for child in fileAST.body:
       if isinstance(child, esprima.nodes.VariableDeclaration):
           attributes.extend(_extract_attribute(child, file_name))
       elif isinstance(child, esprima.nodes.FunctionDeclaration):
            functions.append(_extract_funcdef(child, file_name))
       else:
           log.info("Unknown AST child: " + str(type(child)))
    class_object.methods = functions
    class_object.attributes = attributes
    class_object.inner_classifiers = inner_classifiers
    return class_object


def _extract_attribute(decl, file_name):
    if isinstance(decl, esprima.nodes.VariableDeclaration):
        attributes = []
        for declaration in decl.declarations:
            if isinstance(declaration, esprima.nodes.VariableDeclarator):
                attr_name = IdentifierString(file_name, declaration.id.name)
                a_type = IdentifierString(file_name, "")
                line = _extract_line(declaration)
                attributes.append(Attribute(a_type, attr_name, IdentifierString(file_name, ""), IdentifierString(file_name, ""), line))
            else:
                log.info("Unknown declation type: " + str(type(declaration)))
        return attributes
    else:
        log.error("Unknown top level declaration type: " + str(type(decl)))
        return None

def _extract_funcdef(funcdef, file_name):
    params = _traverse_params(funcdef.params, file_name)
    # return_type = _extract_type_decl(func_decl.type, file_name)
    line = _extract_line(funcdef)
    method_obj = Method(return_type=IdentifierString(file_name, ""), name=IdentifierString(file_name, funcdef.id.name),
                  comment=IdentifierString(file_name, ""),
                  body=IdentifierString(file_name, ""), left_side_identifiers=IdentifierString(file_name, ""),
                  parameters=params, line=line)
    #method_obj.body = IdentifierString(file_name, *_extract_compound(funcdef.body, file_name))
    return method_obj

def _traverse_params(func_decl, file_name):
    params = []
    for param in func_decl:
        if isinstance(param, esprima.nodes.Identifier):  # The ... param
            params.append(_extract_parameter(param, file_name))
        else:
            log.info("Unknown Param Type: " + str(type(param)))
    return params


def _extract_parameter(param, file_name):
    assert isinstance(param, esprima.nodes.Identifier), type(param)
    name = param.name
    #p_type = _extract_type_decl(param.type, file_name)
    return Parameter(IdentifierString(file_name, ""), IdentifierString(file_name, name))


def _extract_identifiertype(id_type, file_name):
    return IdentifierString(file_name, *id_type.names)


def _extract_line(node):
    return node.loc.start.line
