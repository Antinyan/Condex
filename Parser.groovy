import org.grails.datastore.mapping.query.Query

import java.lang.reflect.Array

/**
 * Created by Harkas on 4/9/2017.
 */
class Parser {
    static def content
    static def blockL
    static def filePath
    static functionMap

    Parser(def content, def filePath){
        blockL = []
        this.filePath = filePath
        functionMap = []
        parse(content)
    }

    static def bracketsParser(def fileContent){
        def brackesId=0
        while(fileContent.find(RegExp.brackesPattern)?.size()>0){
            def brackesList = fileContent.findAll(RegExp.brackesPattern)
            fileContent = fileContent.replaceAll(RegExp.brackesPattern,"####")
            def replaceBrackesId=0
            while(fileContent.contains("####")){
                def enterCount = brackesList[replaceBrackesId].findAll("\n").size()
                fileContent = fileContent.replaceFirst("####","\n"*enterCount+"###"/*+brackesId*/)
            }
        }
        return fileContent
    }

    def parse(def content){
        try{
        def blockDept=0
        content=content.replaceAll("\\\\\\\\"," ")
        content=content.replaceAll("\\\\\\\""," ")
        content=content.replaceAll("\\\\\\'"," ")
        content=content.replaceAll("://"," ")
        content=content.replaceAll(RegExp.commentPattern," ")
        content=bracketsParser(content)

        def blockId=0
        def deepestBlockCount = 0
        while(content.find(RegExp.blockPattern)?.size()>0){
            blockDept++
            def blockList=content.findAll(RegExp.blockPattern)
            content = content.replaceAll(RegExp.blockPattern,"@@@@@")
            def deepestBlock = 0
                while(content.contains("@@@@@")){
                    content = content.replaceFirst("@@@@@",/*calculateDigits(null, blockList[deepestBlockCount],blockDept)*/"@@@@"+blockId)

                    Block block = new Block();
                    block.blockId  = blockId
                    block.content =blockList[deepestBlock]
                    def  innerBlockList = blockList[deepestBlock].toString().findAll("@@@@\\d{1,}")
//                    block.innerBlocks = blockList[deepestBlock]?.toString()?.findAll("@@@@\\d{1,}")/*.toString().replaceAll("@@@@","")*/
                    for(def i=0;i<innerBlockList.size(); i++ ){
                        block.innerBlocks<< Integer.parseInt( innerBlockList[i]?.split("@@@@")[1])
                    }
                    block.complexity = 9
                    block.blockDepth=blockDept
//                    block.calculateCharacteristics()
                    deepestBlock++
                    blockL.add(blockId,block)
                    calculateCharacteristics(blockId,block.content)
                    blockId++
                }
            findInnerBlocksTypes(content)
            def file1 = new File('D:\\test\\result\\groovy'+blockDept+'.txt')
            file1<<content
        }
            //TODO  parse block Lists
            //TODO find all preprocessors & count preprocessors complexity
            //TODO find all threads & count threads complexity
        return content
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    static calculateComplexity(def id,def functionSize,def c, def subBlockList, int depth){
        depth = depth*2
        def newSubblockList = []
        for(def i = 0 ; i <subBlockList.size(); i++){
            functionSize = functionSize +blockL[subBlockList[i]]?.functionSize
            c = c + depth + blockL[subBlockList[i]]?.complexity
            if(blockL[subBlockList[i]].innerBlocks.size()>0){
                for(int j = 0 ; j<blockL[subBlockList[i]]?.innerBlocks?.size(); j++){
                    newSubblockList<<blockL[subBlockList[i]]?.innerBlocks[j]
                }

            }
        }
        if (newSubblockList.size()==0){
            blockL[id]?.complexity = c
            blockL[id]?.functionSize = functionSize
            return null
        }
        calculateComplexity(id,functionSize,c, newSubblockList,depth)
    }

    static def findInnerBlocksTypes(String cont){
        cont.findAll("\\sif\\s*###\\s*@@@@\\d{1,}").each {it->
            blockL[Integer.parseInt(it.findAll("\\d{1,}")[0])].blockType = "condition"
            cont = cont.replaceAll("\\sif\\s*###\\s*@@@@\\d{1,}","here_was_been_condition")
        }
        cont.findAll(RegExp.noFunctionPattern).each {it->
            cont = cont.replaceAll(RegExp.noFunctionPattern,"")
        }

        cont.findAll(RegExp.loopPatternDecl).each {it->
            cont = cont.replaceAll(RegExp.loopPatternDecl,"")
        }

        cont.findAll(RegExp.functionDeclaration).each { it->
            def innerSubBlockIDs = []
            def haraberakanDepth = 1

            def blockiD = Integer.parseInt(it.findAll("\\d{1,}")[0])

            for(def i = 0;i<blockL[blockiD].innerBlocks.size();i++){
                if(blockL[blockiD].innerBlocks.size()>0){
                }
            }

            functionMap<< [complexity: blockiD,
                           innerBlocks:blockL[blockiD].innerBlocks.toString(),
                           fn:it.split("###")[0],
                           decisionsCount:blockL[blockiD].decisionsCount,
                           loopCount:blockL[blockiD].loopCount,
                           varCount:blockL[blockiD].varCount,
                           varDecList:blockL[blockiD].varDecList,
                           complexity:blockL[blockiD].complexity,
                           varAllList:blockL[blockiD].varAllList.toString()/*loopCount*/,
                           content:blockL[blockiD].content.toString()/*loopCount*/,
                           GV:getGlobalVariables(blockL[blockiD].innerBlocks,blockL[blockiD].varAllList,blockL[blockiD].varDecList).join("\\r\\n")/*loopCount*/
            ]

            blockL[blockiD].varGlobalList =getGlobalVariables(blockL[blockiD].innerBlocks,blockL[blockiD].varAllList,blockL[blockiD].varDecList)
            blockL[blockiD].blockType = "function"
            blockL[blockiD].name = it.split("###")[0]


           calculateComplexity(blockiD,blockL[blockiD]?.functionSize,blockL[blockiD]?.complexity, blockL[blockiD]?.innerBlocks,1)
        }
    }
    static def getGlobalVariables(def innerBlocksIds, def allVarList, def localVarsList ){
        def innerIds = []
        for(def i = 0 ; i <innerBlocksIds.size(); i++){
            localVarsList = localVarsList+blockL[innerBlocksIds[i]].varDecList
            allVarList = allVarList+blockL[innerBlocksIds[i]].varAllList
            if(blockL[innerBlocksIds[i]].innerBlocks.size()>0){
                for(int j = 0 ; j<blockL[innerBlocksIds[i]]?.innerBlocks?.size(); j++){
                    innerIds<<blockL[innerBlocksIds[i]]?.innerBlocks[j]
                }
            }

        }
        if (innerIds.size()==0){
            return allVarList - localVarsList
        }
        getGlobalVariables(innerIds,allVarList,localVarsList)
    }

    static def calculateCharacteristics(def blockId, String content){
        def allContent = content
        def thr =content.findAll("\\sthread")
        if(thr.size()>0){
            println(thr)
        }
        blockL[blockId]?.preprocessorsCount = content.findAll(RegExp.preprocessorPattern).size()
        content = content.replaceAll(RegExp.preprocessorPattern ,"  ")
        blockL[blockId]?.loopCount = content.findAll(RegExp.loopPatternDecl).size()
        content = content.replaceAll(RegExp.loopPatternDecl ,"  ")
        blockL[blockId]?.decisionsCount = content.findAll(/*"\\sif\\s*###\\s*@@@@\\d{1,}"*/RegExp.decisionPattern).size()
        content = content.replaceAll(RegExp.decisionPattern ,"  ")
        blockL[blockId]?.weakDecision = content.findAll(RegExp.weakDecisionPattern).size()
        content = content.replaceAll(RegExp.weakDecisionPattern ,"  ")
        blockL[blockId]?.operatorsCount = content.findAll(RegExp.operatorPattern).size()
        blockL[blockId]?.functionCall = content.findAll(RegExp.functionCallPattern).size()
        content = content.replaceAll(RegExp.functionCallPattern, "  ")
        blockL[blockId]?.varCount = 0
        def allVarsInBlock = content.findAll(RegExp.varDecPattern)
        for(def i = 0 ; i < allVarsInBlock.size();i++){

            if(!RegExp.keywords.containsKey(allVarsInBlock[i].find("\\s+[a-zA-Z_]*[\\w]+").replaceAll("\\s",""))){
                blockL[blockId]?.varDecList << allVarsInBlock[i].findAll("\\s+[a-zA-Z_]*[\\w]+")[-1].replaceAll("\\s","")
                blockL[blockId]?.varCount =  blockL[blockId]?.varCount +1
            }
        }
        content = content.replaceAll(RegExp.varDecPattern,"  ")

        def allVars = content.findAll(RegExp.varPattern);
        for (def i = 0; i<allVars.size();i++){
            if(!RegExp.keywords.containsKey(allVars[i])){
               blockL[blockId].varAllList<<allVars[i].replaceAll("\\s","").split("\\.")[0]
            }
        }

        blockL[blockId].complexity =2*blockL[blockId]?.loopCount + 2*blockL[blockId]?.decisionsCount + 1*blockL[blockId]?.weakDecision + 1*blockL[blockId]?.operatorsCount+1*blockL[blockId]?.functionCall +1*blockL[blockId]?.varCount
        blockL[blockId].functionSize = blockL[blockId]?.loopCount + blockL[blockId]?.decisionsCount + blockL[blockId]?.weakDecision + blockL[blockId]?.operatorsCount+blockL[blockId]?.functionCall +blockL[blockId]?.varCount
        return content
    }

}
