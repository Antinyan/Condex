class Config {
    def language = ["C","C++"]
    def fileExtensions = []
    def foundFilesList = []
    static  characteristicsWeight = ["Operators":1,"localVariiables":1,"GlobalVariables":21,"weakBranching":2,"branching":3,"looping":7,"functionCalls":1,"pointers":3]
    Config(def language, def folder){
        explore(folder)
    }

    def explore(String path) throws NullPointerException, FileNotFoundException, IOException {
//        Map map = new HashMap()
        File root = new File(path);
        def list = root.listFiles();
        for(int i = 0; i < list.size(); i++)
        {
            if(list[i].isDirectory())
            {
                explore(list[i].getAbsolutePath());
                continue;
            }
            def fileName = list[i].getName().toString()
            if(fileName.endsWith(".cpp")||fileName.endsWith(".h")||fileName.endsWith(".c")||
                    fileName.endsWith(".hpp")||fileName.endsWith(".cxx")||
                    fileName.endsWith(".hxx")||fileName.endsWith(".cc"))
            {
                foundFilesList<<list[i].getAbsolutePath()
            }
        }

    }

}
