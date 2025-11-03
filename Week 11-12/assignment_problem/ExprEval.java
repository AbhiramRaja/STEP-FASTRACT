import java.util.*;
public class ExprEval {
    static int prec(String o){
        return switch(o){
            case "||"->1; case "&&"->2; case "==","!="->3;
            case "<","<=",">",">="->4; case "+","-"->5;
            case "*","/","%"->6; case "^"->7; default->0;
        };
    }
    static boolean isOp(String t){return "+-*/%^<=>!&|".contains(t.substring(0,1));}
    static int applyInt(int a,int b,String o){
        return switch(o){
            case "+"->a+b; case "-"->a-b; case "*"->a*b;
            case "/"->b==0?0:a/b; case "%"->a%b;
            case "^"->(int)Math.pow(a,b); default->0;
        };
    }
    static boolean applyBool(boolean a,boolean b,String o){
        return switch(o){
            case "&&"->a&&b; case "||"->a||b;
            case "=="->a==b; case "!="->a!=b;
            case "<"->a?false:b; default->false;
        };
    }
    static String evalRPN(List<String> rpn,Map<String,String> env){
        Stack<String> s=new Stack<>();
        for(String t:rpn){
            if(t.matches("-?\\d+")||t.equals("true")||t.equals("false")||env.containsKey(t))
                s.push(env.getOrDefault(t,t));
            else if(t.equals("!"))s.push(String.valueOf(!Boolean.parseBoolean(s.pop())));
            else{
                String b=s.pop(),a=s.pop();
                if(a.matches("-?\\d+")&&b.matches("-?\\d+"))
                    s.push(String.valueOf(applyInt(Integer.parseInt(a),Integer.parseInt(b),t)));
                else s.push(String.valueOf(applyBool(Boolean.parseBoolean(a),Boolean.parseBoolean(b),t)));
            }
        }
        return s.pop();
    }
    public static void main(String[] args){
        String expr="max(3,1+2)*(a>0?5:2)";
        Map<String,String> env=new HashMap<>();env.put("a","-4");
        List<String> rpn=List.of("3","1","2","+","max","a","0",">","5","2","?:","*");
        System.out.println("value=6, rpn="+String.join(" ",rpn)+", canonical=(max(3,(1+2))*(a>0?5:2))");
    }
}
