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
    Boolean save=true;
    String tokenString;

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
        while(!state.equals("done")){
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
                    else if (content[correntLo]==' '||content[correntLo]=='\t'||content[correntLo]=='\n'){
                        save=false;
                    }
                    else if (content[correntLo]=='/'){
                        state="slash";
                    }
                    else {
                        state="done";
                        switch (content[correntLo]){
                            case '+':
                                currentToken="plus";
                                break;
                            case '-':
                                currentToken="minus";
                                break;
                            case '*':
                                currentToken="times";
                                break;
                            case ';':
                                currentToken="semi";
                                break;
                            case ',':
                                currentToken="comma";
                                break;
                            case '(':
                                currentToken="lparen";
                                break;
                            case ')':
                                currentToken="rparen";
                                break;
                            case '{':
                                currentToken="lblock";
                                break;
                            case '}':
                                currentToken="rblock";
                                break;
                            default:
                                currentToken="error";
                                break;
                        }
                    }
                    break;
                case "slash":      //
                    if (content[correntLo]=='*'){
                        state="comment";
                        save=false;
                    }
                    else{
                        state="done";
                        currentToken="over";
                        correntLo--;
                    }
                    break;
                case "comment":
                    if (content[correntLo]=='*'){
                        state="outcomment";
                    }
                    break;
                case "outcomment":
                    if (content[correntLo]=='/'){
                        state="start";
                    }
                    else {
                        state="comment";
                    }
                    break;
                case "inse":
                    if (content[correntLo]=='='){
                        state="done";
                        currentToken="lessequal";
                    }
                    else{
                        state="done";
                        currentToken="less";
                        correntLo--;
                    }
                    break;
                case "inle":
                    if (content[correntLo]=='='){
                        state="done";
                        currentToken="morequal";
                    }
                    else{
                        state="done";
                        currentToken="more";
                        correntLo--;
                    }
                    break;
                case "ine":
                    if (content[correntLo]=='='){
                        state="done";
                        currentToken="equal";
                    }
                    else {
                        state="done";
                        currentToken="assign";
                        correntLo--;
                    }
                    break;
                case "innote":
                    if (content[correntLo]=='='){
                        state="done";
                        currentToken="unequal";
                    }
                    else {
                        state="done";
                        currentToken="error";
                        correntLo--;
                    }
                    break;
            }
            if (save){
                tokenString+=content[correntLo];
            }
            if (state=="done"){
                if (currentToken=="id"){
                    currentToken=findPreserve(tokenString);
                }
            }
            correntLo++;    //往后读一个位置
        }
        return currentToken;
    }

    public String findPreserve(String str){
        String[] preserve={"else","if","int","return","void","while"};
        for(String s:preserve){
            if (s.equals(str))return s;
        }
        return "id";
    }

    public static void main(String[] args) throws IOException {
        Scanner s=new Scanner(System.in);
        String str=s.nextLine();
        LexicalAnalysis lexicalAnalysis=new LexicalAnalysis(str);
    }
}

