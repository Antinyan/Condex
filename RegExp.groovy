

import java.util.regex.Pattern

class RegExp {
    static FileWriter writer
    static Pattern loopPattern = ~/\sfor\s*\(|\swhile\s*\(/
    static Pattern loopPatternDecl = ~/\sfor\s*###\s*@@@@\d{1,}|\swhile\s*###\s*@@@@\d{1,}/
    static Pattern preprocessorPattern = ~/#+[ ]*[\w]+/
//    static Pattern decisionPattern = ~/\sif\s*\(|\selse\s*\(|\scase\s+|\sswitch\s*\(|\scatch\s*\(|\stry\s*@|\?/
    static Pattern decisionPattern = ~/\sif\s*\#{3}|\selse\s*|\?/
//    static Pattern functionCallPattern = ~/[\:\.\w][\w\d:]+\s*\(/
    static Pattern functionCallPattern = ~/[\:\.a-zA-Z_]+[\w:]*\s*\#\#\#/
    static Pattern functionDeclarationPattern = ~/[^\S]+\**[a-zA-Z_:\.~@]+[\w:\.~@]*[\s]*(\*|==|!=|<=|>=|>|<|&&|\|\||!|&|\||\^|~|>>|<<)*[\s]*(@@@)+\s*((const)|(throw[\(:w]*)|(:\s*[\w@,\s]+))*\s*@&@[@&\-\d]+/
    static Pattern functionDeclaration = ~/[^\S]+\**[a-zA-Z_:\.~@]+[\w:\.~@]*[\s]*(\*|==|!=|<=|>=|>|<|&&|\|\||!|&|\||\^|~|>>|<<)*[\s]*(###)+\s*((const)|(throw[\(:w]*)|(:\s*[\w@,\s]+))*\s*@@@@\d{1,}/
    static Pattern operatorPattern = ~/[^-]-[^-]|[^\+]\+[^\+]|\*|\/|%|\+\+|--|==|!=|<=|>=|>|<|&&|\|\||!|&|\||\^|~|>>|<</
    static Pattern varPattern = ~/([a-zA-Z_]+[\w]*\.{0,1}[a-zA-Z_]+[\w]*)|([a-zA-Z_]+[\w]*)/
    static Pattern numberFormatPattern = ~/@&@[@&\-\d]+/
    static Pattern commentPattern = ~/(\"{3,}[\S\s]+?"{3,})|(\/\*[\s\S]*?\*\/)|(\/\/.*)|("[^\r]*?")|('[^\r]*?')/
    static Pattern blockPattern = ~/\{[^\{]*?\}/
    static Pattern brackesPattern = ~/\([^(]*?\)/
    static Pattern noFunctionPattern = ~/\sthrow\s*\#\#\#|\sfor\s*\#\#\#|\swhile\s*\#\#\#|\sswitch\s*\#\#\#|\sif\s*\#\#\#|\sdo\s*\#\#\#|\selse\s*\#\#\#/

    static Pattern classDeclaration = ~/\s*class\s*@@@@\d/
    static Pattern namespaceDeclaration = ~/\s*namespace\s*@@@@\d/
    static Pattern varDecPattern =  ~/\s+[a-zA-Z_]+[\w]*\*{0,1}\s+[a-zA-Z_]+[\w]*/
//    static Pattern weakDecisionPattern = ~/\scase\s+|\sswitch\s*\(|\scatch\s*\(|\stry\s*###|\?/
    static Pattern weakDecisionPattern = ~/\scase\s+|\sswitch\s*\#{3}|\scatch\s*\#{3}|\stry\s*/
    static def keywords = ["auto":"",
                           "break":"",
                           "case":"",
//                "char":"",
                           "const":"",
                           "continue":"",
                           "default":"",
                           "do":"",
//                "double":"",
                           "else":"",
                           "enum":"",
                           "extern":"",
//                "float":"",
                           "for":"",
                           "goto":"",
                           "if":"",
                           "false":"",
                           "true":"",
//                "int":"",
//                "long":"",
                           "register":"",
                           "return":"",
                           "short":"",
                           "signed":"",
                           "sizeof":"",
                           "static":"",
                           "struct":"",
                           "switch":"",
                           "typedef":"",
                           "union":"",
                           "unsigned":"",
                           "void":"",
                           "volatile":"",
                           "while":"",
                           "new":"",
                           "void":"",
                           "class":""]
}
