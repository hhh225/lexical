package mainPack;

public class Test {
    public static void main(String[] args) {
        char[] chars={'1'};
        String token="token\0token";
        switch (chars[0]){
            case '0':
                System.out.println("0");
                break;
            case '1':
                System.out.println("1");
                System.out.println("1");
                break;
        }
        String str="hello";
        System.out.println(str=="hello");
        System.out.println(token);
    }
}
