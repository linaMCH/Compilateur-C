%%

%public
%class Analyseur_lex
%unicode
%type String
%line
%column

%{
  private void printToken(String name) {
    System.out.println("<" + name + ", '" + yytext() + "'> à la ligne " + (yyline+1) + ", col " + (yycolumn+1));
  }
%}

/* EXP REGULIERE */
Lettre           = [a-zA-Z_]
Chiffre          = [0-9]
Identificateur   = {Lettre}({Lettre}|{Chiffre})*
Entier           = {Chiffre}+
Reel         = {Chiffre}+ \. {Chiffre}* | \. {Chiffre}+
Espace           = [ \t\f\r\n]
Commentaire      = "//" .* | "/*" ([^*] | \*+ [^/*])* \*+ "/"

%%

<YYINITIAL> {

  /* MOTS CLES */
  "int"      { printToken("INT"); return "INT"; }
  "float"    { printToken("FLOAT"); return "FLOAT"; }
  "char"     { printToken("CHAR"); return "CHAR"; }
  "void"     { printToken("VOID"); return "VOID"; }
  "return"   { printToken("RETURN"); return "RETURN"; }
  "if"       { printToken("IF"); return "IF"; }
  "else"     { printToken("ELSE"); return "ELSE"; }
  "for"      { printToken("FOR"); return "FOR"; }
  "while"    { printToken("WHILE"); return "WHILE"; }
  "do"       { printToken("DO"); return "DO"; }

  /* OPERATEURS ARETHMETIQUES */
  "++"       { printToken("INC"); return "INC"; }
  "--"       { printToken("DEC"); return "DEC"; }
  "+"        { printToken("PLUS"); return "PLUS"; }
  "-"        { printToken("MOINS"); return "MOINS"; }
  "*"        { printToken("MULT"); return "MULT"; }
  "/"        { printToken("DIV"); return "DIV"; }

  /* OPERATEURS DE COMPARAISON */
  "=="       { printToken("EQL"); return "EQL"; }
  "="        { printToken("AFFECT"); return "AFFECT"; }
  "<"        { printToken("INF"); return "INF"; }
  ">"        { printToken("SUP"); return "SUP"; }
  "<="       { printToken("INF_EQL"); return "INF_EQL"; }
  ">="       { printToken("SUP_EQL"); return "SUP_EQL"; }

  /* SEPARATEURS */
  ","        { printToken("COMMA"); return "COMMA"; }
  ";"        { printToken("SEMICOL"); return "SEMICOL"; }
  "."        { printToken("DOT"); return "DOT"; }
  "("        { printToken("OUV_PAREN"); return "OUV_PAREN"; }
  ")"        { printToken("FER_PAREN"); return "FER_PAREN"; }
  "{"        { printToken("CROCH_OUV"); return "CROCH_OUV"; }
  "}"        { printToken("CROCH_FER"); return "CROCH_FER"; }

  /* IDENTIFICATEURS ET NOMBRES */
  {Identificateur} { printToken("ID"); return "ID"; }
  {Entier}         { printToken("NUM_INT"); return "NUM_INT"; }
  {Reel}       { printToken("NUM_FLOAT"); return "NUM_FLOAT"; }

  /* INGNORER LES ESPACES ET COMMENTAIRES */
  {Espace}       { /* ignore */ }
  {Commentaire}  { /* ignore */ }
  }
