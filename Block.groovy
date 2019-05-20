

/**
 * Created by Harkas on 4/9/2017.
 */
class Block {
//    def namespace
//    def parentClass
    def name
    def blockId
    def blockDepth
//    def parentblock
    def innerBlocks = [] //TODO SAVE All blocks' ids
//    def startLine
//    def endLine
    def blockType
    def blockComplexity
    def content                         //Block content
    def blockSize
    def digits///////////
    def bracketsContent                 // (def a, ... def b)

    def preprocessorsCount
    int loopCount
    int weakDecision
    int functionCall
    int decisionsCount
    def functionCallsCount
    def operatorsCount
    def varCount
    def varDecList = []
    def varAllList = []
    def varGlobalList = []  //Delete
    def complexity
    def functionSize
    def bracketsMap =[:]


    public void calculateCharacteristics(){

       this?.loopCount = content.findAll(RegExp.loopPatternDecl).size()
       this?.decisionsCount = content.findAll("\\sif\\s*###\\s*@@@@\\d{1,}").size()
       this?.varCount = 0
        def allVarsInBlock = content.findAll(RegExp.variablePattern)
        for(def i = 0 ; i < allVarsInBlock.size();i++){
            if(!RegExp.keywords.containsKey(allVarsInBlock[i].find("\\s+[a-zA-Z_]*[\\w]+").replaceAll("\\s",""))){
                this?.variablesList << allVarsInBlock[i]
                this?.varCount = this?.varCount +1
            }
        }
        this.complexity =5*this?.loopCount + 4*this?.decisionsCount +  3*this?.varCount
////        if (content.contains("@&@")){
////            this.digits = content.findAll(RegExp.numberFormatPattern).join("")
////        }
//        this.preprocessorsCount = content.findAll(RegExp.preprocessorPattern).size()
//        content = content.replaceAll(RegExp.preprocessorPattern,"")
//        this.loopCount = content.findAll(RegExp.loopPattern).size()
//        content = content.replaceAll(RegExp.loopPattern,"")
//        this.decisionsCount = content.findAll(RegExp.decisionPattern).size()
//        content = content.replaceAll(RegExp.decisionPattern,"")
//        this.functionCallsCount = content.findAll(RegExp.functionCallPattern).size()
//        this.operatorsCount = content.findAll(RegExp.operatorPattern).size()
//        this.varCount = content.findAll(RegExp.varPattern).size()
//        this.blockComplexity = 4*preprocessorsCount + 7*loopCount + 4*decisionsCount + 2*functionCallsCount + operatorsCount + 2*varCount
//        this.functionSize = preprocessorsCount + loopCount + decisionsCount + functionCallsCount + operatorsCount + varCount
//
//        getInnerBlockTypesFunctionDeclaration(content)

    }


    static def getInnerBlockTypesFunctionDeclaration(String blockCode){
        def list
//        while(blockCode.find(RegExp.brackesPattern)?.size()>0){
//            blockCode = blockCode.findAll(RegExp.brackesPattern,"###")
//        }
        list = blockCode.findAll(RegExp.loopPatternDecl)
        blockCode.findAll(RegExp.functionCallPattern)
        println blockCode

    }

}
