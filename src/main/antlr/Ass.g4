grammar Ass;

stat: (typeSpec)* EOF;

typeSpec: typeSpecString
 |typeSpecNumber
 |typeSpecDecimal
 |typeSpecBoolean
;

typeSpecString: IDENTIFIER ':' TYPE_STRING (DEFAULT STRING_VALUE)? (OPTS stringValueList)? ';';
typeSpecNumber: IDENTIFIER ':' TYPE_NUMBER (DEFAULT NUMBER_VALUE)? (OPTS numberValueList)? ';';
typeSpecDecimal: IDENTIFIER ':' TYPE_DECIMAL (DEFAULT DECIMAL_VALUE)? (OPTS decimalValueList)? ';';
typeSpecBoolean: IDENTIFIER ':' TYPE_BOOLEAN (DEFAULT BOOL_VALUE)? ';';

TYPE_STRING: 'String'|'String?';
TYPE_NUMBER: 'Number'|'Number?';
TYPE_BOOLEAN: 'Boolean'|'Boolean?';
TYPE_DECIMAL: 'Decimal'|'Decimal?';

NUMBER_VALUE: [0-9]+;
numberValueList: '['NUMBER_VALUE(','NUMBER_VALUE)*']';

DECIMAL_VALUE: [0-9]+'.'[0-9]+;
decimalValueList: '['DECIMAL_VALUE(','DECIMAL_VALUE)*']';

BOOL_VALUE: 'true'|'false';

STRING_VALUE: '"' (~'"')*? '"'  ;
stringValueList: '['STRING_VALUE(','STRING_VALUE)*']';

OPTS: 'in';
DEFAULT: 'default';
IDENTIFIER : [a-z_][A-Za-z0-9_]*;

WS: [ \r\n\t]+ -> skip;
