```java
import java.util.*;
public class Game24 {
	static int[] number = new int[5];
	static boolean[] count = new boolean[14];
	static String Solution = new String();
	static char[] operator = { '$', '+', '-', '*', '/' };
	static Map<Integer, Integer> check = new HashMap<Integer, Integer>();
	private static int Calculate(int a, int b, char op) {
		switch(op) {
		case '+':return a + b;
		case '-':return a - b;
		case '*':return a * b;
		case '/':
			if(b == 0 || a % b != 0) break;
			return a / b;
		default:break;
		}
		return 0x3f3f3f3f;
	}
	private static void Swap(int[] array, int x, int y) {
		if(array[x] == array[y]) return;
		array[x] ^= array[y];
		array[y] ^= array[x];
		array[x] ^= array[y];
	}
	private static boolean NextPermutation(int[] array, int left, int right) {
		int cur = right - 1, pre = cur - 1;
		while(cur > left && array[cur] <= array[pre]) {
			--cur;
			--pre;
		}
		if(cur <= left) return false;
		cur = right - 1;
		while(array[cur] <= array[pre]) --cur;
		Swap(array, cur, pre);
		int len = pre + 1 + right;
		int mid = len >> 1;
		for(int i = pre + 1; i < mid; ++i) {
			Swap(array, i, len - i - 1);
		}
		return true;
	}
	private static int Hash(int[] array) {
		return array[1] + array[2] + array[3] + array[4];
	}
	private static boolean GetAnswer(int[] array) {
		int a = array[1], b = array[2], c = array[3], d = array[4];
		boolean flag = false;
		for(int i = 1; i <= 4; ++i) {
			for(int j = 1; j <= 4; ++j) {
				for(int k = 1; k <= 4; ++k) {
					if (Calculate(Calculate(a, b, operator[i]), Calculate(c, d, operator[j]), operator[k]) == 24) {
						flag = true;
						Solution = "(" + a + " " + operator[i] + " " + b + "" + ")" + " " + operator[j] + " " + "(" + c + " " + operator[k] + " " + d + ")";
						}
					else if (Calculate(Calculate(Calculate(a, b, operator[i]), c, operator[j]), d, operator[k]) == 24) {
						flag = true;
						Solution = "((" + a + " " + operator[i] + " " + b + "" + ")" + " " + operator[j] + " "  + c + ") " + operator[k] + " " + d + "";
						}
					else if (Calculate(Calculate(a, Calculate(b, c, operator[j]), operator[i]), d, operator[k]) == 24) {
						flag = true;
						Solution = "(" + a + " " + operator[i] + " (" + b + " " + operator[j] + " " + c + ")) " + operator[k] + " " + d + "";
						}
					else if (Calculate(a, Calculate(b, Calculate(c, d, operator[k]), operator[j]), operator[i]) == 24) {
						flag = true;
						Solution = a + " " + operator[i] + " (" + b + " " + operator[j] + " (" + c + " " + operator[k] + " " + d + "))";
						}
					else if (Calculate(a, Calculate(Calculate(b, c, operator[j]), d, operator[k]), operator[i]) == 24) {
						flag = true;
						Solution = a + " " + operator[i] + " ((" + b + " " + operator[j] + " " + c + ") " + operator[k] + " " + d + ")";
						}
					if(flag) {
						check.put(Hash(array), 1);
						break;
					}
				}
				if(flag) break;
			}
			if(flag) break;
		}
		return flag;
	}
	private static void MakeNumbers() {
		Random rand = new Random();
		boolean tag = false;
		while(!tag) {
			for(int i = 1; i <= 4; ++i) {
				number[i] = rand.nextInt(14);
			}
			if(check.containsKey(Hash(number))) continue;
			Arrays.sort(number);
			while(NextPermutation(number, 1, 5)) {
				tag = GetAnswer(number);
				if(tag) break;
			}
		}
		for(int i = 1; i <= 4; ++i) {
			System.out.print(number[i] + " ");
		}
		System.out.print("\nInput 1 to know the answer or input 2 to exit.\n");
	}
	private static void ShowSolution() {
		System.out.println("The answer is:");
		System.out.println(Solution);
	}
	private static boolean CheckNumber(int x) {
		if(x < 0 || x > 13) return false;
		for(int i = 0; i <= 4; ++i) {
			if(number[i] == x && !count[i]) {
				count[i] = true;
				return true;
			}
		}
		return false;
	}
	private static String[] ChangeToSuffixExpression(String infix) {
		char[] temp = infix.toCharArray();
		String[] suffix = new String[110];
		int len = temp.length;
		int cnt = -1;
		Stack<Character> sta = new Stack<Character>();
		for(int i = 0; i < len; ++i) {
			char c = temp[i];
			if('0' <= c && c <= '9') {
				suffix[++cnt] += c;
				if(i + 1 < len && '0' <= temp[i + 1] && temp[i + 1] <= '9') {
					suffix[cnt] += temp[i + 1];
					++i;
				}
			}
			else if(c == '(') {
				sta.push(c);
			}
			else if(c == ')') {
				while(!sta.empty() && sta.peek() != '(') {
					suffix[++cnt] += sta.pop();
				}
				sta.pop();
			}
			else if(c == '+' || c == '-') {
				while(!sta.empty() && sta.peek() != '(') {
					suffix[++cnt] += sta.pop();
				}
				sta.push(c);
			}
			else if(c == '*' || c == '/') {
				while(!sta.empty() && (sta.peek() == '*' || sta.peek() == '/')) {
					suffix[++cnt] += sta.pop();
				}
				sta.push(c);
			}
		}
		while(!sta.empty()) suffix[++cnt] += sta.pop();
		return suffix;
	}
	private static boolean CheckAnswer(String s) {
		String[] suffix = ChangeToSuffixExpression(s);
		Arrays.fill(count, false);
		int len = suffix.length;
		Stack<Integer> sta = new Stack<Integer>();
		for(int i = 0; i < len; ++i) {
			if(suffix[i] == null) break;
			char[] c = suffix[i].toCharArray();
			if(c[4] == '+' || c[4] == '-' || c[4] == '*' || c[4] == '/') {
				int a = sta.pop(), b = sta.pop();
				sta.push(Calculate(b, a, c[4]));
			}
			else {
				int l = c.length;
				int t = 0;
				if(l == 5) t = c[4] - '0';
				else t = (c[4] - '0') * 10 + c[5] - '0';
				if(!CheckNumber(t)) {
					System.out.println("Please using the giving number!");
					return false;
				}
				sta.push(t);
			}
		}
		return sta.pop() == 24;
	}
	private static void Run24Game() {
		Scanner input = new Scanner(System.in);
		while(true) {
			MakeNumbers();
			boolean tag = false;
			while(!tag) {
				String s = input.nextLine();
				if(s.contentEquals("1")) {
					ShowSolution();
					break;
				}
				else if(s.contentEquals("2")) {
					System.out.println("Thanks for your use.");
					input.close();
					return;
				}
				else {
					tag = CheckAnswer(s);
					if(tag) System.out.println("Congragulation! Your answer is right.");
					else System.out.println("Sorry, your answer is wrong.");
				}
			}
		}
	}
	public static void main(String[] args) {
		Run24Game();
	}
}

```
