public class kuir {

    public static void main(String[] args){
        String command = args[0];
        String path = args[1];
        String query = null;
        if(args.length>2){
            query = args[3];
        }

        if(command.equals("-c")){
            makeCollection collection = new makeCollection(path);
            collection.makeXml();
        }
        else if(command.equals("-k")){
            makeKeyword keyword = new makeKeyword(path);
            keyword.convertXml();
        }
        else if(command.equals("-i")){
            indexer indexer = new indexer(path);
            indexer.makePost();
        }
        else if(command.equals("-s")){
            searcher searcher = new searcher(path,query);
            searcher.printTitle();
        }
        else if(command.equals("-m")){
            MidTerm MidTerm = new MidTerm(path,query);
            MidTerm.showSnippet();
        }
    }
}