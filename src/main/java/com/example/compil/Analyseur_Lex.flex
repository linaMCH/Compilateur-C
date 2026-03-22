%%

%public
%class Analyseur_lex
%unicode
%type Token
%line
%column

%{
import com.example.compil.Token;

private Token token(String type){
    return new Token(type, yytext(), yyline+1, yycolumn+1);
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
  "int"      { return token("INT"); }
  "float"    { return token("FLOAT"); }
  "double"   { return token("DOUBLE"); }
  "const"    { return token("CONST"); }
  "char"     { return token("CHAR"); }
  "void"     { return token("VOID"); }

  "return"   { return token("RETURN"); }
  "if"       { return token("IF"); }
  "else"     { return token("ELSE"); }
  "for"      { return token("FOR"); }
  "while"    { return token("WHILE"); }
  "do"       { return token("DO"); }
  "switch"   { return token("SWITCH"); }
  "case"     { return token("CASE"); }
  "print"      { return token("PRINT"); }

  "#include" { return token("INCLUDE"); }
  "#define"  { return token("DEFINE"); }

  /* OPERATEURS */
  "++" { return token("INC"); }
  "--" { return token("DEC"); }
  "+"  { return token("PLUS"); }
  "-"  { return token("MOINS"); }
  "*"  { return token("MULT"); }
  "/"  { return token("DIV"); }

  /* COMPARAISON */
  "==" { return token("EQL"); }
  "="  { return token("AFFECT"); }
  "<"  { return token("INF"); }
  ">"  { return token("SUP"); }
  "<=" { return token("INF_EQL"); }
  ">=" { return token("SUP_EQL"); }

  /* LOGIQUE */
  "&&" { return token("AND"); }
  "||" { return token("OR"); }
  "!"  { return token("NOT"); }

  /* SEPARATEURS */
  "," { return token("COMMA"); }
  ";" { return token("SEMICOL"); }
  ":" { return token("COLON"); }

  "(" { return token("OUV_PAREN"); }
  ")" { return token("FER_PAREN"); }

  "{" { return token("CROCH_OUV"); }
  "}" { return token("CROCH_FER"); }

  /* IDENTIFICATEURS */
  {Identificateur} { return token("ID"); }

  /* NOMBRES */
  {Entier} { return token("NUM_INT"); }
  {Reel}   { return token("NUM_FLOAT"); }

  /* IGNORER */
  {Espace} { }
  {Commentaire} { }

  /* ERREUR LEXICALE */
  . {
      System.out.println(
        "Erreur lexicale : '" + yytext() +
        "' ligne " + (yyline+1)
      );
  }

}
