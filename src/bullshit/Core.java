package bullshit;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Core {
//    For the actual file.

    private static ArrayList<Word> textDirty = new ArrayList<>();
    private static ArrayList<Word> textCleaned = new ArrayList<>();
    private static HashMap<String, Word> wordsActual = new HashMap<>();

//    For the reference file.
    private static ArrayList<Word> textCleanedRef = new ArrayList<>();
    private static HashMap<String, Word> wordsRef = new HashMap<>();
//    For semantic analysis
    private static ArrayList<Word> namesList = new ArrayList<>();
    private static ArrayList<Word> namesListFinal = new ArrayList<>();
    private static ArrayList<String> text = new ArrayList<>();
    
//    For UI
    boolean isSemantic;

    public static void mainMethod() throws FileNotFoundException {

        boolean runStop=false;
        while(runStop!=true){
        runProgram();

            System.out.println("Szeretné újra futatni a programot?");
            Scanner sc= new Scanner(System.in);
            String choose= sc.nextLine();
            
             if(choose.equals("igen")){
                 runProgram();
             } if(choose.equals("nem")){
                 runStop=true;
             
            } else {
                 System.out.println("Kérem igennel vagy nemmel válaszoljon.");
                 String choose2= sc.nextLine();
             }
    }

    }
    
    private static void readFile(String actual, String reference) throws FileNotFoundException {

        Scanner sc = new Scanner(new File(reference));
        while (sc.hasNext()) {
            String s1 = sc.next().trim().toLowerCase();
            s1 = cleanSpecialCharacters(s1);
            Word wordRefClean = new Word();

            wordRefClean.setWord(s1);
            String a = wordRefClean.getWord();
            if (wordsRef.containsKey(a)) {
                wordsRef.get(wordRefClean.getWord()).setCounter();
            } else if (wordRefClean.getWord() != " ") {
                wordsRef.put(wordRefClean.getWord(), wordRefClean);
            }
        }
        for (Map.Entry<String, Word> entry : wordsRef.entrySet()) {
            textCleanedRef.add(entry.getValue());
        }
        sortByOccurrence(textCleanedRef);

        ArrayList<String> conjuctions = new ArrayList<>(topXxx((textCleanedRef), 150));

        Scanner sc2 = new Scanner(new File(actual));
        while (sc2.hasNext()) {
            String s = sc2.next().trim();
            Word wordDirty = new Word();
            wordDirty.setWord(s);
            textDirty.add(wordDirty);

            s = cleanSpecialCharacters(s).toLowerCase();

            Word word = new Word();
            word.setWord(s);
            String b = word.getWord();

            if (conjuctions.contains(word.getWord()) == false) {
                text.add(word.getWord());
                if (wordsActual.containsKey(b)) {

                    wordsActual.get(word.getWord()).setCounter();
                } else if (word.getWord() != " ") {
                    wordsActual.put(word.getWord(), word);
                }
            }
        }
        for (Map.Entry<String, Word> entry : wordsActual.entrySet()) {
            textCleaned.add(entry.getValue());
        }
        sortByOccurrence(textCleaned);

    }

    private static String cleanSpecialCharacters(String s) {
        String[] chars = {"?", "!", ".", ",", ":", "-", ";", "–"};
        return cleanSpecialCharacters(s, chars);
    }

    private static String cleanSpecialCharacters(String s, String[] specialChars) {
        for (String aChar : specialChars) {
            s = s.replace(aChar, "");
        }
        return s;
    }

    private static void sortByOccurrence(ArrayList<Word> a) {
        ListComparator byOccurrence = new ListComparator();
        Collections.sort(a, byOccurrence);

    }

    private static ArrayList<String> topXxx(ArrayList<Word> a, int b) {

        ArrayList<String> topXxx = new ArrayList<>();
        for (int i = 0; i < b; i++) {
            topXxx.add(a.get(i).getWord());
        }
        return topXxx;
    }
    
    private static ArrayList<String> topXxxOfSpeechs(ArrayList<OfSpeech> a, int b) {

        ArrayList<String> topXxx = new ArrayList<>();
        for (int i = 0; i < b; i++) {
            topXxx.add(a.get(i).getOfSpeech());
        }
        return topXxx;
    }

    private static ArrayList<OfSpeech> generateOfSpeeches(int n) {

        int real = n;
        ArrayList<OfSpeech> temp = new ArrayList<>();
        HashMap<String, OfSpeech> ofSpeech = new HashMap<>();
        for (int i = 1; i < text.size() - real; i++) {
            if (text.get(i).trim() != " ") {

                String s = text.get(0 + i) + " ";
                for (int j = 1; j < real; j++) {

                    String c = text.get(i + j).trim();
                    s += c + " ";
                }

                OfSpeech tem = new OfSpeech();
                tem.setOfSpeech(s);
                if (ofSpeech.containsKey(s)) {
                    ofSpeech.get(tem.getOfSpeech()).setCounter();

                } else {
                    ofSpeech.put(tem.getOfSpeech(), tem);

                }
            }
        }
        for (Map.Entry<String, OfSpeech> entry : ofSpeech.entrySet()) {
            temp.add(entry.getValue());

        }
        ListComparatorForOfSpeeches byOccurrence = new ListComparatorForOfSpeeches();
        Collections.sort(temp, byOccurrence);

        return temp;
    }

    private static String cleanMostUsedNames(String s) {
        String[] chars = {"?", "!", ".", ",", ":", ";"};
        return cleanMostUsedNames(s, chars);
    }

    private static String cleanMostUsedNames(String s, String[] specialChars) {
        for (String aChar : specialChars) {
            s = s.replace(aChar, "");
        }
        return s;
    }

    private static void getNames() {
        String[] s = {".", "?", "!"};
        int counter = 1;
        while (counter < textDirty.size() - 1) {
            counter++;
            do {
                if (Character.isUpperCase(textDirty.get(counter).getWord().charAt(0))) {
                    namesList.add(textDirty.get(counter));
                }
                counter++;
                if (counter == textDirty.size()) {
                    break;
                }
            } while (textDirty.get(counter).getWord().contains(s[0])
                    || textDirty.get(counter).getWord().contains(s[1])
                    || textDirty.get(counter).getWord().contains(s[2]));
        }
        for (int i = 0; i < namesList.size(); i++) {
            namesList.get(i).setWord(cleanMostUsedNames(namesList.get(i).getWord()));
            if (namesList.get(i).getWord().endsWith("-")) {
                String name = namesList.get(i).getWord().substring(0, namesList.get(i).getWord().length() - 1);
                namesList.get(i).setWord(name);
            }
            int n = 0;
            if (!namesListFinal.isEmpty()) {
                for (int j = 0; j < namesListFinal.size(); j++) {
                    if (namesListFinal.get(j).getWord().equals(namesList.get(i).getWord())) {
                        namesListFinal.get(j).setCounter();
                        break;
                    } else {
                        n++;
                    }
                }
            } else {
                namesListFinal.add(namesList.get(i));
            }
            if (n == namesListFinal.size()) {
                namesListFinal.add(namesList.get(i));
            }
        }
        ListComparator namesByCounter = new ListComparator();
        Collections.sort(namesListFinal, namesByCounter);
    }

    private static String generateBullShit(int a, int b) {

        String bullShitTemp = "";
        
        int s=a+b;
        int indexFixer=0;
        int v=s+indexFixer;
        for (int i = a-1; i <= v; i++) {
            String c = textCleaned.get(i).getWord().trim();
            
            
            if(bullShitTemp.contains(c)){
                indexFixer++;
            }
            else{
            bullShitTemp += c + " ";}
        }
        return bullShitTemp;
    }
    
    private static void makeSemantic(){
        
        
        System.out.println("Dolgozunk.");
        ArrayList<String> topWords = new ArrayList<>(topXxx((textCleaned), 10));
        System.out.println("A szövegben ez a 10 leggyakoribb szó.\n"+topWords);
        System.out.println("");
        ArrayList<String> ofSpeeches2 = new ArrayList<>(topXxxOfSpeechs(generateOfSpeeches(2), 10));
        System.out.println("A szövegben ez a 10 leggyakoribb kétszavas szóforudlat.\n"+ofSpeeches2);
        System.out.println("");
        ArrayList<String> ofSpeeches3 = new ArrayList<>(topXxxOfSpeechs(generateOfSpeeches(3), 10));
        System.out.println("A szövegben ez a 10 leggyakoribb három szavas szóforudlat.\n"+ofSpeeches3);
        System.out.println("");
        ArrayList<String> ofSpeeches4 = new ArrayList<>(topXxxOfSpeechs(generateOfSpeeches(4), 10));
        System.out.println("A szövegben ez a 10 leggyakoribb négy szavas szóforudlat.\n"+ofSpeeches4);
        System.out.println("");
        getNames();
        ArrayList<String> names = new ArrayList<>(topXxx(namesListFinal, 10));
        System.out.println("A szövegben ez a 10 leggyakoribb név.\n"+names);
        System.out.println("");
        
        

    }
    
    private static void makeBullShit(int a/*belépési érték*/, int b/*intervalum*/){
        String bullShit = generateBullShit(a, b).trim();
       bullShit =bullShit.substring(0, 1).toUpperCase() + bullShit.substring(1)+".";
        System.out.println(bullShit);
    }
    
    private static void runProgram() throws FileNotFoundException{
        
        Scanner sc = new Scanner(System.in);
        System.out.println("Az alábbiakban szemantikai elemzést végezhet egy szövegen,\n"
                + "vagy random mondatot(BullShit-et) generáltathat a programmal.\n"
                + "Kérem adja meg az elemezendő fájl (Amely txt formátumú.) teljes elérési útvonalát.");
        String path=sc.nextLine();
        String actual =path;
        System.out.println("Amennyiben meg szeretne adni saját referencia szöveget (Amely txt formátumú.) az összehasonlításhoz.\n"
                + "Adja meg annak is a teljes elérési útvonalát.Ellenkező esetben kérem nyomja meg a 0-át\n"
                + "és a program a beépített referencia szöveget fogja használni.");
        String pathRef=sc.nextLine();
        String zeroPath=Integer.toString(0);
        
        String refTemp;
        if(pathRef.equals(zeroPath)){
            refTemp = "ref.txt";
        }else{
             refTemp = pathRef;
        }
            readFile(actual, refTemp);
            
        System.out.println("Kérem válasszon a billentyűzete segítségével.\n 1-es "
                + "gomb szemantikai elemezés, 2-es gomb BullShit generálás.");
        int SemOrBull=sc.nextInt();
        if(SemOrBull==2){
            System.out.println("Kérlem válasszon, hogy a szőveg hanyadik szavától "
                    + "szeretné indítani a generálást.");
            int startingPoint=sc.nextInt();
            System.out.println("Kérem adja meg hogy hányszavas Bullshit "
                    + "mondatot szeretne generálni."
                    + "Ha nem tudodja eldöntei akkor nyomja meg a 0-át\nés a program kidobja önnek "
                    + "a lehető leghosszabb mondatot ami generálható az ön által megadott szóhoz képest.");
            int lengthOfBullShit=sc.nextInt();
            System.out.println("A BullShit mondat: ");
           makeBullShit(startingPoint,lengthOfBullShit);
        }else{
        makeSemantic();
        }
    }
    
}
