grammar ass;

program: variable+ EOF;
variable: strvar|intvar|boolvar|decimalvar;
vardef: IDENTIFIER TYPESPEC;
strvar: vardef 'String''?'? (DEFAULT STRING_VALUE)? (OPTS STRING_VALUE_LIST)? EOL;
intvar: vardef 'Number''?'? (DEFAULT NUMBER_VALUE)? (OPTS NUMBER_VALUE_LIST)? EOL;
decimalvar: vardef 'Decimal''?'? (DEFAULT DECIMAL_VALUE)? (OPTS DECIMAL_VALUE_LIST)? EOL;
boolvar: vardef 'Boolean''?'? (DEFAULT BOOL_VALUE)? EOL;

IDENTIFIER : [a-z_][A-Za-z0-9_]*;
TYPESPEC: ':';
OPTS: 'in';
DEFAULT: 'default';
EOL: ';';

NUMBER_VALUE: [0-9]+;
NUMBER_VALUE_LIST: '['NUMBER_VALUE(','NUMBER_VALUE)*']';

BOOL_VALUE: 'true'|'false';
DECIMAL_VALUE: [0-9]+'.'[0-9]+;
DECIMAL_VALUE_LIST: '['DECIMAL_VALUE(','DECIMAL_VALUE)*']';

STRING_VALUE: '"' (~'"')*? '"'  ;
STRING_VALUE_LIST: '['STRING_VALUE(','STRING_VALUE)*']';


WS: [ \r\n\t]+ -> skip;
