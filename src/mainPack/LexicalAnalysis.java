package mainPack;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LexicalAnalysis {
    String state="start";
    File file;
    FileInputStream fileInputStream;
    InputStreamReader inputStreamReader;
    StringBuffer stringBuffer;
    char[] content;
    Boolean save=true;
    String tokenString="";
    int correntLo=0;

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
        //int correntLo=0;
        String currentToken=null;
        while(!state.equals("done")){

            switch (state){
                case "start":
                    save=true;
                    if (correntLo==content.length){
                        return "end";
                    }
                    if (Character.isDigit(content[correntLo])){         //数字
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
                    else if (content[correntLo]==' '||content[correntLo]=='\t'||content[correntLo]=='\r'){
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

                    }
                    else{
                        state="done";
                        save=false;
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
                        state="done";
                        tokenString="";
                        save=false;
                    }
                    else {
                        state="comment";
                    }
                    break;
                case "inse":
                    if (correntLo==content.length){
                        state="done";
                        currentToken="less";
                        save=false;
                    }
                    else if (content[correntLo]=='='){
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
                    if (correntLo==content.length){
                        state="done";
                        currentToken="more";
                        save=false;
                    }
                    else if (content[correntLo]=='='){
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
                    if (correntLo==content.length){
                        state="done";
                        currentToken="assign";
                        save=false;
                    }
                    else if (content[correntLo]=='='){
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
                    if (correntLo==content.length){
                        state="done";
                        currentToken="error";
                        save=false;
                    }
                    else if (content[correntLo]=='='){
                        state="done";
                        currentToken="unequal";
                    }
                    else {
                        state="done";
                        currentToken="error";
                        correntLo--;
                    }
                    break;
                case "innum":
                    if (correntLo==content.length){
                        state="done";
                        save=false;
                        currentToken="num";


                    }
                    else if (!Character.isDigit(content[correntLo])){    //如果识别到一个不是数字的
                        state="done";
                        save=false;
                        currentToken="num";     //
                        correntLo--;   //往后退一位
                    }
                    break;
                case "inid":
                    if (correntLo==content.length){
                        state="done";
                        save=false;
                        currentToken="id";


                    }
                    else if (!Character.isAlphabetic(content[correntLo])){
                        state="done";
                        save=false;
                        currentToken="id";
                        correntLo--;
                    }
                    break;
                case "done":
                    break;
                default:
                    state="done";
                    currentToken="error";
            }
            if (save){         //保存，那么保存当前位
                tokenString+=content[correntLo];
            }

            if (state=="done"){
                if (currentToken=="id"){
                    currentToken=findPreserve(tokenString);
                }

                System.out.println(tokenString);
                System.out.println(correntLo);
            }
            correntLo++;    //往后读一个位置
        }
        state="start";
        tokenString="";
        return currentToken;
    }

    public String findPreserve(String str){
        String[] preserve={"else","if","int","return","void","while"};
        for(String s:preserve){
            if (s.equals(str))return "reserveWord";
        }
        return "id";
    }

    public void process(){
        List<String> symbolTable=new ArrayList<String>();
        while (correntLo<content.length){
            String lexicalUnit=stateTransform();
            if(!lexicalUnit.equals("end"))symbolTable.add(lexicalUnit);
        }
        System.out.println("识别到的词法单元:");
        for(String s:symbolTable){
            System.out.println(s);
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner s=new Scanner(System.in);
        String str=s.nextLine();
        LexicalAnalysis lexicalAnalysis=new LexicalAnalysis(str);
        lexicalAnalysis.process();
    }
}

