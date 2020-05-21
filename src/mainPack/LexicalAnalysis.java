package mainPack;

import java.io.*;
import java.util.Scanner;

public class LexicalAnalysis {
    String state="start";
    File file;
    FileInputStream fileInputStream;
    InputStreamReader inputStreamReader;
    StringBuffer stringBuffer;
    char[] content;

    public LexicalAnalysis(String rute) throws IOException { //构造函数读文件
        file=new File(rute);
        if(!file.exists()){
            System.out.println("the file does not exist");
        }

        fileInputStream = new FileInputStream(file);
        inputStreamReader=new InputStreamReader(fileInputStream);
        stringBuffer=new StringBuffer();
        while (inputStreamReader.ready()){
            stringBuffer.append((char)inputStreamReader.read());
        }
        content=stringBuffer.toString().toCharArray();
    }

    public String stateTransform(){
        int correntLo=0;
        String currentToken=null;
        while(true){
            switch (state){
                case "start":
                    if (Character.isDigit(content[correntLo])){
                        state="innum";
                    }
                    else if (Character.isAlphabetic(content[correntLo])){
                        state="inid";
                    }
                    else if (content[correntLo]=='<'){
                        state="inse";
                    }
                    else if(content[correntLo]=='>'){
                        state="inle";
                    }
                    else if (content[correntLo]=='='){
                        state="ine";
                    }
                    else if (content[correntLo]=='!'){
                        state="innote";
                    }
                    else if (content[correntLo]=='')
                    break;
            }
            break;
        }
        return currentToken;
    }
    public static void main(String[] args) throws IOException {
        Scanner s=new Scanner(System.in);
        String str=s.nextLine();
        LexicalAnalysis lexicalAnalysis=new LexicalAnalysis(str);
    }
}

