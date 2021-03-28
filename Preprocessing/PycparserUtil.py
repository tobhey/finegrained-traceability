from logging import getLogger
import logging, FileUtil
from Preprocessing.CodeFileRepresentation import *
import pycparser

log = getLogger(__name__)
logging.basicConfig(level=logging.INFO)
"""
Contains functions to parse the AST returned by the pycparser into a code file representation

"""
def extract_FileAST(fileAST, filepath):
    assert isinstance(fileAST, pycparser.c_ast.FileAST)
    file_name = FileUtil.get_filename_from_path(filepath)
    #Use mock class to hold the methods
    class_name = IdentifierString(file_name, "")
    class_object = Classifier(class_name, IdentifierString(file_name, ""))
    attributes = []
    functions = []
    inner_classifiers = []
    for child in fileAST.ext:
        if file_name == _extract_filename_from_coord(child.coord, file_name):#Ignore childs included by #include
            if isinstance(child, pycparser.c_ast.Typedef):
                if isinstance(child.type, pycparser.c_ast.PtrDecl): # It's a pointer ...
                    if isinstance(child.type.type, pycparser.c_ast.FuncDecl): # ... to a function
                        functions.append(_extract_func_declaration(child.type.type, child.name, file_name))
                    else: # normal pointer
                        attributes.append(_extract_pointer_attribute(child.type, child.name, file_name)) 
                elif isinstance(child.type.type, pycparser.c_ast.Enum):
                    inner_classifiers.append(_extract_enum(child.type, file_name))
                elif isinstance(child.type, pycparser.c_ast.TypeDecl):
                    attributes.append(_extract_attribute(child, file_name))
                else:
                    log.info("Ignoring typedef: " + str(child.name))
            elif isinstance(child, pycparser.c_ast.Decl):
                if isinstance(child.type, pycparser.c_ast.FuncDecl): #It's a function without body
                    functions.append( _extract_func_declaration(child.type, child.name, file_name))
                elif isinstance(child.type, pycparser.c_ast.PtrDecl):
                    if isinstance(child.type.type, pycparser.c_ast.FuncDecl): # function pointer
                        functions.append(_extract_func_declaration(child.type.type, child.name, file_name))
                else: # It's an attribute-like type definition
                    attributes.append(_extract_attribute(child, file_name))
            elif isinstance(child, pycparser.c_ast.FuncDef): # It's a function with body
                functions.append( _extract_funcdef(child, file_name))
            else:
                log.info("Unknown FileAST child: " + str(type(child)))
    class_object.methods = functions
    class_object.attributes = attributes
    class_object.inner_classifiers = inner_classifiers
    return class_object
    
def _extract_funcdef(funcdef, file_name):
    _extract_filename_from_coord(funcdef.coord, file_name)
    method_obj = _extract_func_declaration(funcdef.decl.type, funcdef.decl.name, file_name)
    method_obj.body = IdentifierString(file_name, *_extract_compound(funcdef.body, file_name))
    return method_obj
    
def _extract_parameter(param, file_name):
    assert isinstance(param, pycparser.c_ast.Decl), type(param)
    name = param.name
    p_type = _extract_type_decl(param.type, file_name)
    return Parameter(p_type, IdentifierString(file_name, name))
    
def _extract_identifiertype(id_type, file_name):
    return IdentifierString(file_name, *(id_type.names))

def _extract_line(coord, file_name):
    assert isinstance(coord, pycparser.plyparser.Coord)
    return coord.line
    
def _extract_compound(comp, file_name):
    assert isinstance(comp, pycparser.c_ast.Compound), type(comp)
    all_body_strings = []
    if comp.block_items is None:
        return []
    for child in comp.block_items:
        all_body_strings += _extract_string_from_body_element(child, file_name)
    return all_body_strings

def _extract_string_from_body_element(elem, file_name):
    if elem is None:
        return [] 
    elif isinstance(elem, pycparser.c_ast.ArrayRef):
        return _extract_string_from_body_element(elem.name, file_name) + _extract_string_from_body_element(elem.subscript, file_name)
    elif isinstance(elem, pycparser.c_ast.FuncCall):
        return _extract_string_from_body_element(elem.name, file_name) + _extract_string_from_body_element(elem.args, file_name)
    elif isinstance(elem, pycparser.c_ast.If):
        return _extract_string_from_body_element(elem.cond, file_name) + _extract_string_from_body_element(elem.iftrue, file_name) + _extract_string_from_body_element(elem.iffalse, file_name)
    elif isinstance(elem, pycparser.c_ast.While):
        return _extract_string_from_body_element(elem.cond, file_name) + _extract_string_from_body_element(elem.stmt, file_name)
    elif isinstance(elem, pycparser.c_ast.For):
        return _extract_string_from_body_element(elem.init, file_name) + _extract_string_from_body_element(elem.cond, file_name) \
        + _extract_string_from_body_element(elem.next, file_name) + _extract_string_from_body_element(elem.stmt, file_name)
    elif isinstance(elem, pycparser.c_ast.DoWhile):
        return _extract_string_from_body_element(elem.cond, file_name) + _extract_string_from_body_element(elem.stmt, file_name)
    elif isinstance(elem, pycparser.c_ast.BinaryOp):
        return _extract_string_from_body_element(elem.left, file_name) +  _extract_string_from_body_element(elem.right, file_name)
    elif isinstance(elem, pycparser.c_ast.ExprList):
        return _extract_string_from_body_element(elem.exprs, file_name)
    elif isinstance(elem, pycparser.c_ast.Goto):
        return _extract_string_from_body_element(elem.name, file_name)
    elif isinstance(elem, pycparser.c_ast.ID):
        return _extract_string_from_body_element(elem.name, file_name)
    elif isinstance(elem, pycparser.c_ast.Label):
        return _extract_string_from_body_element(elem.name, file_name) + _extract_string_from_body_element(elem.stmt, file_name)
    elif isinstance(elem, pycparser.c_ast.Return):
        return _extract_string_from_body_element(elem.expr, file_name)
    elif isinstance(elem, pycparser.c_ast.Compound):
        return _extract_compound(elem, file_name)
    elif isinstance(elem, pycparser.c_ast.Assignment):
        return _extract_string_from_body_element(elem.lvalue, file_name) + _extract_string_from_body_element(elem.rvalue, file_name)
    elif isinstance(elem, pycparser.c_ast.Break):
        return []
    elif isinstance(elem, pycparser.c_ast.Continue):
        return []
    elif isinstance(elem, pycparser.c_ast.EmptyStatement):
        return []
    elif isinstance(elem, pycparser.c_ast.Case):
        return _extract_string_from_body_element(elem.expr, file_name) + _extract_string_from_body_element(elem.stmts, file_name)
    elif isinstance(elem, pycparser.c_ast.Cast):
        return _extract_string_from_body_element(elem.to_type, file_name) + _extract_string_from_body_element(elem.expr, file_name)
    elif isinstance(elem, pycparser.c_ast.Enumerator):
        return _extract_string_from_body_element(elem.name, file_name) + _extract_string_from_body_element(elem.value, file_name)
    elif isinstance(elem, pycparser.c_ast.EnumeratorList):
        return _extract_string_from_body_element(elem.enumerators, file_name)
    elif isinstance(elem, pycparser.c_ast.InitList):
        return _extract_string_from_body_element(elem.exprs, file_name)
    elif isinstance(elem, str):
        return [elem]
    elif isinstance(elem, list):
        strings = []
        for e in elem:
            strings += _extract_string_from_body_element(e, file_name)
        return strings
    elif isinstance(elem, pycparser.c_ast.Decl):
        return _extract_string_from_body_element(elem.name, file_name) + _extract_string_from_body_element(elem.type, file_name) + _extract_string_from_body_element(elem.init, file_name)
    elif isinstance(elem, pycparser.c_ast.DeclList):
        return _extract_string_from_body_element(elem.decls, file_name)
    elif isinstance(elem, pycparser.c_ast.Default):
        return _extract_string_from_body_element(elem.stmts, file_name)
    elif isinstance(elem, pycparser.c_ast.Constant):
        return _extract_string_from_body_element(elem.type, file_name) + _extract_string_from_body_element(elem.value, file_name)
    elif isinstance(elem, pycparser.c_ast.CompoundLiteral):
        return _extract_string_from_body_element(elem.type, file_name) + _extract_string_from_body_element(elem.init, file_name)
    elif isinstance(elem, pycparser.c_ast.Constant):
        return _extract_string_from_body_element(elem.type, file_name) + _extract_string_from_body_element(elem.value, file_name)
    elif isinstance(elem, pycparser.c_ast.StructRef):
        return _extract_string_from_body_element(elem.name, file_name) + _extract_string_from_body_element(elem.field, file_name)
    elif isinstance(elem, pycparser.c_ast.Switch):
        return _extract_string_from_body_element(elem.cond, file_name) + _extract_string_from_body_element(elem.stmt, file_name)
    elif isinstance(elem, pycparser.c_ast.TernaryOp):
        return _extract_string_from_body_element(elem.cond, file_name) + _extract_string_from_body_element(elem.iftrue, file_name) + _extract_string_from_body_element(elem.iffalse, file_name)
    elif isinstance(elem, pycparser.c_ast.Typename):
        return _extract_string_from_body_element(elem.name, file_name) + _extract_string_from_body_element(elem.type, file_name)
    elif isinstance(elem, pycparser.c_ast.UnaryOp):
        return _extract_string_from_body_element(elem.expr, file_name)
    elif isinstance(elem, pycparser.c_ast.Union):
        return _extract_string_from_body_element(elem.name, file_name) + _extract_string_from_body_element(elem.decls, file_name)
    elif isinstance(elem, pycparser.c_ast.TypeDecl) or isinstance(elem, pycparser.c_ast.IdentifierType) \
        or isinstance(elem, pycparser.c_ast.ArrayDecl) or isinstance(elem, pycparser.c_ast.PtrDecl) \
        or isinstance(elem, pycparser.c_ast.Struct) or isinstance(elem, pycparser.c_ast.FuncDecl):
        return _extract_type_decl(elem, file_name).tokens
    else:
        log.info("Unknown Body block item: " + str(type(elem)))
        return []

def _extract_func_declaration(func_decl, func_name: str, file_name):
    assert isinstance(func_decl, pycparser.c_ast.FuncDecl), type(func_decl)
    params = _traverse_params(func_decl, file_name)
    return_type = _extract_type_decl(func_decl.type, file_name)
    line = _extract_line(func_decl.coord, file_name)
    return Method(return_type, IdentifierString(file_name, func_name), IdentifierString(file_name, ""),
                   IdentifierString(file_name, ""), IdentifierString(""),  params, line)
    
def _traverse_params(func_decl, file_name):
    assert isinstance(func_decl, pycparser.c_ast.FuncDecl), type(func_decl)
    params = []
    if func_decl.args:
        for param in func_decl.args.params:
            if isinstance(param, pycparser.c_ast.EllipsisParam): # The ... param
                continue
            if isinstance(param, pycparser.c_ast.Typename): # (only?) anonymous type
                params.append(_extract_typename(param, file_name))
            else:
                params.append(_extract_parameter(param, file_name))
    return params

def _extract_typename(typename, file_name):# (only?) anonymous types
    name = typename.name if typename.name else ""
    p_type = _extract_type_decl(typename.type, file_name)
    return Parameter(p_type, IdentifierString(file_name, name))

def _extract_filename_from_coord(coord, file_name):
    assert isinstance(coord, pycparser.plyparser.Coord)
    return FileUtil.get_filename_from_path(coord.file)

def _extract_attribute(decl, file_name):
    if isinstance(decl.type, pycparser.c_ast.ArrayDecl) or isinstance(decl.type, pycparser.c_ast.TypeDecl):
        attr_name = IdentifierString(file_name, decl.name)
        a_type = _extract_type_decl(decl.type, file_name)
        line = _extract_line(decl.coord, file_name)
        return Attribute(a_type, attr_name, IdentifierString(file_name, ""), IdentifierString(file_name, ""), line)
    elif isinstance(decl.type, pycparser.c_ast.PtrDecl):
        return _extract_pointer_attribute(decl.type, decl.name, file_name)
    else:
        log.error("Unknown top level declaration type: " + str(type(decl.type)))
        return None

def _extract_type_decl(type_decl, file_name):
    if isinstance(type_decl.type, pycparser.c_ast.TypeDecl) or \
        isinstance(type_decl.type, pycparser.c_ast.ArrayDecl) or \
        isinstance(type_decl.type, pycparser.c_ast.PtrDecl):
        return _extract_type_decl(type_decl.type, file_name)
    elif isinstance(type_decl.type, pycparser.c_ast.IdentifierType):
        return _extract_identifiertype(type_decl.type, file_name)
    elif isinstance(type_decl.type, pycparser.c_ast.Struct):
        return IdentifierString(file_name, "struct")
    elif isinstance(type_decl.type, pycparser.c_ast.FuncDecl): #Function pointer as param
        return _extract_func_pointer_in_param(type_decl.type, file_name)
    elif isinstance(type_decl.type, pycparser.c_ast.Union):
        return IdentifierString(file_name, type_decl.type.name)
    elif isinstance(type_decl.type, pycparser.c_ast.Enum):
        return IdentifierString(file_name, type_decl.type.name)
    else:
        log.info("Unknown type decl: " + str(type(type_decl.type)))
        return IdentifierString(file_name, "")

def _extract_enum(enum, file_name):
    assert isinstance(enum.type, pycparser.c_ast.Enum), type(enum.type)
    enum_name = enum.declname
    constants = []
    for constant in enum.type.values.enumerators:
        constants.append(IdentifierString(file_name, constant.name))
    line = _extract_line(enum.type.coord, file_name)
    return Enum_(IdentifierString(file_name, enum_name), IdentifierString(file_name, ""), [], [], constants, [], [], line)

def _extract_func_pointer_in_param(func_decl, file_name):
    assert isinstance(func_decl, pycparser.c_ast.FuncDecl), type(func_decl)
    params = _traverse_params(func_decl, file_name)
    return_type = _extract_type_decl(func_decl.type, file_name)
    all_strings = return_type.tokens
    for p in params:
        all_strings += p.get_param_words()
    # Set all params of function pointer args as function pointer type for the method
    return IdentifierString(file_name, *all_strings) 

def _extract_pointer_attribute(pointer_decl, attr_name: str, file_name):
    assert isinstance(pointer_decl, pycparser.c_ast.PtrDecl), type(pointer_decl)
    a_type = _extract_type_decl(pointer_decl.type, file_name)
    line = _extract_line(pointer_decl.coord, file_name)
    return Attribute(a_type, IdentifierString(file_name, attr_name), IdentifierString(file_name, ""), IdentifierString(file_name, ""), line)
    