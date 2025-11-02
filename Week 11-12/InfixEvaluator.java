import java.util.*;

public class InfixEvaluator {
   
    public static int evaluate(String expr, Map<String, Integer> env) {
        try {
            List<String> tokens = tokenize(expr);
            List<String> rpn = toRPN(tokens);
            return evalRPN(rpn, env);
        } catch (Exception e) {
            System.out.println("ERROR");
            return 0;
        }
    }
   
    static List<String> tokenize(String s) {
        List<String> res = new ArrayList<>();
        int i = 0;
        while (i < s.length()) {
            char c = s.charAt(i);
            if (c == ' ') {
                i++;
            } else if (Character.isDigit(c)) {
                String num = "";
                while (i < s.length() && Character.isDigit(s.charAt(i))) {
                    num += s.charAt(i++);
                }
                res.add(num);
            } else if (Character.isLetter(c)) {
                String name = "";
                while (i < s.length() && Character.isLetterOrDigit(s.charAt(i))) {
                    name += s.charAt(i++);
                }
                res.add(name);
            } else if (c == '-' && (res.isEmpty() || res.get(res.size()-1).equals("(") || "+-*/%^,".contains(res.get(res.size()-1)))) {
                res.add("~");
                i++;
            } else {
                res.add("" + c);
                i++;
            }
        }
        return res;
    }
   
    static List<String> toRPN(List<String> tokens) {
        List<String> out = new ArrayList<>();
        Stack<String> st = new Stack<>();
       
        for (String t : tokens) {
            if (t.matches("\\d+") || (t.matches("[a-z]+") && !t.matches("min|max|abs"))) {
                out.add(t);
            } else if (t.matches("min|max|abs")) {
                st.push(t);
            } else if (t.equals(",")) {
                while (!st.peek().equals("(")) out.add(st.pop());
            } else if ("+-*/%^~".contains(t)) {
                while (!st.isEmpty() && prec(st.peek()) >= prec(t) && !t.equals("^") && !t.equals("~")) {
                    out.add(st.pop());
                }
                st.push(t);
            } else if (t.equals("(")) {
                st.push(t);
            } else if (t.equals(")")) {
                while (!st.peek().equals("(")) out.add(st.pop());
                st.pop();
                if (!st.isEmpty() && st.peek().matches("min|max|abs")) out.add(st.pop());
            }
        }
        while (!st.isEmpty()) out.add(st.pop());
        return out;
    }
   
    static int evalRPN(List<String> rpn, Map<String, Integer> env) {
        Stack<Integer> st = new Stack<>();
        for (String t : rpn) {
            if (t.matches("\\d+")) {
                st.push(Integer.parseInt(t));
            } else if (t.matches("[a-z]+")) {
                st.push(env.get(t));
            } else if (t.equals("~")) {
                st.push(-st.pop());
            } else if (t.equals("abs")) {
                st.push(Math.abs(st.pop()));
            } else if (t.equals("min")) {
                int b = st.pop(), a = st.pop();
                st.push(Math.min(a, b));
            } else if (t.equals("max")) {
                int b = st.pop(), a = st.pop();
                st.push(Math.max(a, b));
            } else {
                int b = st.pop(), a = st.pop();
                if (t.equals("+")) st.push(a + b);
                else if (t.equals("-")) st.push(a - b);
                else if (t.equals("*")) st.push(a * b);
                else if (t.equals("/")) st.push(a / b);
                else if (t.equals("%")) st.push(a % b);
                else if (t.equals("^")) st.push((int)Math.pow(a, b));
            }
        }
        return st.pop();
    }
   
    static int prec(String op) {
        if (op.equals("~")) return 4;
        if (op.equals("^")) return 3;
        if ("*/%".contains(op)) return 2;
        if ("+-".contains(op)) return 1;
        return 0;
    }
   
    public static void main(String[] args) {
        System.out.println(evaluate("3 + 4 * 2 / (1 - 5) ^ 2^3", new HashMap<>()));
        System.out.println(evaluate("min(10, max(2, 3*4))", new HashMap<>()));
       
        Map<String, Integer> m = new HashMap<>();
        m.put("x", -2);
        m.put("y", -7);
        System.out.println(evaluate("-(x) + abs(y)", m));
       
        m.clear();
        m.put("a", 1);
        evaluate("a + b", m);
    }
}
