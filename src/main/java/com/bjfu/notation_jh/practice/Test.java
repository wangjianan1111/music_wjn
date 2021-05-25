package com.bjfu.notation_jh.practice;

import org.apache.derby.impl.store.raw.log.Scan;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.ReentrantLock;

public class Test {

    public static void main123(String[] args) {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap();
        map.put("phone", "123");
    }

    public static void main1(String[] args) {
        Test test = new Test();
//        System.out.println(test.fib2(4));
        int[] nums = {1, 2, 3};
        System.out.println(test.premu(nums));
    }

    //备忘录方式求解斐波那契数列
    //f(n)=f(n-1)+f(n-2) f(3)=f(2)+f(1) f(4)=f(3)+f(2)
    private int fib(int n) {
        if (n < 2) return 0;
        int[] memo = new int[n + 1];
        for (int i = 0; i < n + 1; i++) {
            memo[i] = 0;
        }
        return helper(memo, n);
    }

    private int helper(int[] memo, int n) {
        if (n == 1 || n == 2) return 1;
        if (memo[n] != 0) return memo[n];
        memo[n] = helper(memo, n - 1) + helper(memo, n - 2);
        return memo[n];
    }

    //自底向上
    private int fib1(int n) {
        if (n == 1 || n == 2) return 1;
        int[] dp = new int[n + 1];
        for (int i = 0; i < n + 1; i++) {
            dp[i] = 0;
        }
        dp[1] = dp[2] = 1;
        for (int i = 3; i < n + 1; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }

    //自底向上，最优子结构优化，只保存前两个值就可以
    private int fib2(int n) {
        if (n == 1 || n == 2) return 1;
        int pre = 1;
        int cur = 1;
        for (int i = 3; i < n + 1; i++) {
            int sum = pre + cur;
            pre = cur;
            cur = sum;
        }
        return cur;
    }

    private int coinsChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        for (int i = 0; i < amount + 1; i++) {
            dp[i] = amount + 1;
        }
        dp[0] = 0;
        for (int i = 0; i < dp.length; i++) {
            for (int coin : coins) {
                if (i - coin < 0) continue;
                dp[i] = Math.min(dp[i], dp[i - coin] + 1);
            }
        }
        return dp[amount] == amount + 1 ? -1 : dp[amount];
    }

    //全排列问题
    List<List<Integer>> res = new ArrayList<>();

    private List<List<Integer>> premu(int[] nums) {
        List<Integer> track = new LinkedList<>();
        brackTrack(nums, track);
        return res;
    }

    private void brackTrack(int[] nums, List<Integer> track) {
        //结束条件
        if (nums.length == track.size()) {
            res.add(new LinkedList<>(track));
            return;
        }
        //选择
        for (int num : nums) {
            if (track.contains(num)) continue;
            track.add(num);
            brackTrack(nums, track);
            track.remove(track.lastIndexOf(num));
        }
    }

    //N皇后


    //二分查找
    public int binarySearch(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] > target) {
                right = mid - 1;
            } else if (nums[mid] < target) {
                left = mid + 1;
            }
        }
        return -1;
    }

    //寻找左侧边界的二分搜索


    public static void main2(String[] args) {
        Scanner in = new Scanner(System.in);
        int num = in.nextInt();
        String str = in.next();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != 'E' && str.charAt(i) != 'F') {
                return;
            }
        }
        System.out.println(maxDiff(str));
    }

    /**
     * 5
     * EFEEF
     */
    private static int maxDiff(String str) {
        int[] arr = new int[str.length()];
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == 'E') {
                arr[i] = 1;
            } else if (str.charAt(i) == 'F') {
                arr[i] = -1;
            }
        }
        int max = arr[0];
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
            if (sum < 0) {
                sum = 0;
            }
            if (sum > max) {
                max = sum;
            }
        }
        return max;
    }

    public static void main6(String[] args) {
        Scanner in = new Scanner(System.in);
        int[] arr = new int[3];
        int i = 0;
        while (i < arr.length) {
            arr[i] = in.nextInt();
            i++;
        }
        int[][] book = new int[arr[0]][arr[1]];
        boolean[][] flag = new boolean[arr[0]][arr[1]];
        for (int k = 0; k < arr[0]; k++) {
            for (int l = 0; l < arr[1]; l++) {
                flag[k][l] = false;
            }
        }
        for (int j = 0; j < arr[2]; j++) {
            int num = in.nextInt();
            while (num != 13) {
                switch (num) {
                    case 1:
                        in.nextInt();
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    default:
                        return;
                }
            }
        }
    }

    /**
     * 0-1背包问题
     *
     * @param V      背包容量
     * @param N      物品种类
     * @param weight 物品重量
     * @param value  物品价值
     * @return
     */
    public static String ZeroOnePack(int V, int N, int[] weight, int[] value) {

        //初始化动态规划数组
        int[][] dp = new int[N + 1][V + 1];
        //为了便于理解,将dp[i][0]和dp[0][j]均置为0，从1开始计算
        for (int i = 1; i < N + 1; i++) {
            for (int j = 1; j < V + 1; j++) {
                //如果第i件物品的重量大于背包容量j,则不装入背包
                //由于weight和value数组下标都是从0开始,故注意第i个物品的重量为weight[i-1],价值为value[i-1]
                if (weight[i - 1] > j)
                    dp[i][j] = dp[i - 1][j];
                else
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - weight[i - 1]] + value[i - 1]);
            }
        }
        //则容量为V的背包能够装入物品的最大值为
        int maxValue = dp[N][V];
        //逆推找出装入背包的所有商品的编号
        int j = V;
        String numStr = "";
        for (int i = N; i > 0; i--) {
            //若果dp[i][j]>dp[i-1][j],这说明第i件物品是放入背包的
            if (dp[i][j] > dp[i - 1][j]) {
                numStr = i + " " + numStr;
                j = j - weight[i - 1];
            }
            if (j == 0)
                break;
        }
        return numStr;
    }

    /**
     * 第三类背包：多重背包
     *
     * @param args
     */
    public static int manyPack(int V, int N, int[] weight, int[] value, int[] num) {
        //初始化动态规划数组
        int[][] dp = new int[N + 1][V + 1];
        //为了便于理解,将dp[i][0]和dp[0][j]均置为0，从1开始计算
        for (int i = 1; i < N + 1; i++) {
            for (int j = 1; j < V + 1; j++) {
                //如果第i件物品的重量大于背包容量j,则不装入背包
                //由于weight和value数组下标都是从0开始,故注意第i个物品的重量为weight[i-1],价值为value[i-1]
                if (weight[i - 1] > j)
                    dp[i][j] = dp[i - 1][j];
                else {
                    //考虑物品的件数限制
                    int maxV = Math.min(num[i - 1], j / weight[i - 1]);
                    for (int k = 0; k < maxV + 1; k++) {
                        dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - k * weight[i - 1]] + k * value[i - 1]);
                    }
                }
            }
        }
        /*//则容量为V的背包能够装入物品的最大值为
        int maxValue = dp[N][V];
        int j=V;
        String numStr="";
        for(int i=N;i>0;i--){
            //若果dp[i][j]>dp[i-1][j],这说明第i件物品是放入背包的
            while(dp[i][j]>dp[i-1][j]){
                numStr = i+" "+numStr;
                j=j-weight[i-1];
            }
            if(j==0)
                break;
        }*/
        return dp[N][V];
    }

    /**
     * 第二类背包：完全背包
     * 思路分析：
     * 01背包问题是在前一个子问题（i-1种物品）的基础上来解决当前问题（i种物品），
     * 向i-1种物品时的背包添加第i种物品；而完全背包问题是在解决当前问题（i种物品）
     * 向i种物品时的背包添加第i种物品。
     * 推公式计算时，f[i][y] = max{f[i-1][y], (f[i][y-weight[i]]+value[i])}，
     * 注意这里当考虑放入一个物品 i 时应当考虑还可能继续放入 i，
     * 因此这里是f[i][y-weight[i]]+value[i], 而不是f[i-1][y-weight[i]]+value[i]。
     *
     * @param V
     * @param N
     * @param weight
     * @param value
     * @return
     */
    public static String completePack(int V, int N, int[] weight, int[] value) {
        //初始化动态规划数组
        int[][] dp = new int[N + 1][V + 1];
        //为了便于理解,将dp[i][0]和dp[0][j]均置为0，从1开始计算
        for (int i = 1; i < N + 1; i++) {
            for (int j = 1; j < V + 1; j++) {
                //如果第i件物品的重量大于背包容量j,则不装入背包
                //由于weight和value数组下标都是从0开始,故注意第i个物品的重量为weight[i-1],价值为value[i-1]
                if (weight[i - 1] > j)
                    dp[i][j] = dp[i - 1][j];
                else
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - weight[i - 1]] + value[i - 1]);
            }
        }
        //则容量为V的背包能够装入物品的最大值为
        int maxValue = dp[N][V];
        int j = V;
        String numStr = "";
        for (int i = N; i > 0; i--) {
            //若果dp[i][j]>dp[i-1][j],这说明第i件物品是放入背包的
            while (dp[i][j] > dp[i - 1][j]) {
                numStr = i + " " + numStr;
                j = j - weight[i - 1];
            }
            if (j == 0)
                break;
        }
        return numStr;
    }

    /**
     * 完全背包的第二种解法
     * 思路：
     * 只用一个一维数组记录状态，dp[i]表示容量为i的背包所能装入物品的最大价值
     * 用顺序来实现
     */
    public static int completePack2(int V, int N, int[] weight, int[] value) {

        //动态规划
        int[] dp = new int[V + 1];
        for (int i = 1; i < N + 1; i++) {
            //顺序实现
            for (int j = weight[i - 1]; j < V + 1; j++) {
                dp[j] = Math.max(dp[j - weight[i - 1]] + value[i - 1], dp[j]);
            }
        }
        return dp[V];
    }

    //单链表是否有环
    public static boolean hasCircle(LNode L) {
        if (L == null) return false;//单链表为空时，单链表没有环
        if (L.next == null) return false;//单链表中只有头结点，而且头结点的next为空，单链表没有环
        LNode p = L.next;//p表示从头结点开始每次往后走一步的指针
        LNode q = L.next.next;//q表示从头结点开始每次往后走两步的指针
        while (q != null) //q不为空执行while循环
        {
            if (p == q) return true;//p与q相等，单链表有环
            p = p.next;
            q = q.next.next;
        }
        return false;
    }

    public static void main9(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        String s = sc.next();
        ArrayList<Integer> arr = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            arr.add(s.charAt(i) - '0');
        }
        int index = 0;
        int n = 0;
        if (N == 1) {
            System.out.println(0);
        } else if (N == 2) {
            System.out.println(1);
        } else {
            while (index < arr.size() && arr.size() > (index + arr.get(index))) {
                ArrayList<Integer> newarr = getnewarr(arr, index);
                int max = getmax(newarr);
                index = geti(arr, index, max) + 1;
                n++;
            }
        }
        System.out.println(n);
    }

    private static int geti(ArrayList<Integer> arr, int index, int max) {
        int desc = maxindex(arr, max) - index;
        if (desc < arr.get(index) && desc > 0) {
            return maxindex(arr, max);
        } else {
            return index + arr.get(index);
        }
    }

    private static int maxindex(ArrayList<Integer> arr, int max) {
        int index = 0;
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i) == max) {
                index = i;
            }
        }
        return index;
    }


    private static int getmax(ArrayList<Integer> newarr) {
        int max = 0;
        for (int i = 0; i < newarr.size(); i++) {
            if (newarr.get(i) <= max) {
                max = newarr.get(i);
            }
        }
        return max;
    }

    private static ArrayList<Integer> getnewarr(ArrayList<Integer> arr, int index) {
        ArrayList<Integer> newarr = new ArrayList<>();
        for (int i = index; i < arr.size(); i++) {
            newarr.add(arr.get(i));
        }
        return newarr;
    }

    public static void main10(String[] args) {
        Scanner sc = new Scanner(System.in);
        int M = sc.nextInt();
        int N = sc.nextInt();
        System.out.println(C(N, M));
    }

    private static int A(int m, int n) {
        int result = 1;
        for (int i = m; i > 0; i--) {
            result *= n;
            n--;
        }
        return result;
    }

    private static int C(int m, int n) {
        int half = n / 2;
        if (m > half) {
            m = n - m;
        }
        int num1 = A(m, n);
        int num2 = A(m, m);
        return num1 / num2;
    }

    public static void main87(String[] args) {
        Scanner sc = new Scanner(System.in);
        int total = sc.nextInt();
        String[] arr1 = new String[total];
        int[] arr2 = new int[total];
        for (int i = 0; i < total; i++) {
            arr1[i] = sc.next();
        }
        for (int i = 0; i < total; i++) {
            String str = sc.next();
            int temp = 0;
            if ("a".equals(str)) {
                temp = 1;
            } else if ("b".equals(str)) {
                temp = 2;

            } else if ("c".equals(str)) {
                temp = 3;

            } else if ("d".equals(str)) {
                temp = 4;

            } else if ("e".equals(str)) {
                temp = 5;

            } else if ("f".equals(str)) {
                temp = 6;

            } else if ("g".equals(str)) {
                temp = 7;

            }
            arr2[i] = temp;
        }
        System.out.println(sort(arr2));
    }

    private static int sort(int[] arr2) {
        int count = 0;
        boolean flag = false;
        for (int i = 0; i > arr2.length; i++) {
            flag = false;
            for (int j = i; j > arr2.length - 1; j++) {
                if (arr2[j] > arr2[j + 1]) {
                    int temp = arr2[j - 1];
                    arr2[j - 1] = arr2[j];
                    arr2[j] = temp;
                    flag = true;
                }
            }
            if (flag == true) {
                count++;
            } else {
                break;
            }
        }
        return arr2.length - count;
    }

    public int findFriendNum(int[][] M) {
        // write code here
        int len = M.length;
        int[] visitied = new int[len];
        int res = 0;
        for (int i = 0; i < M.length; i++) {
            if (visitied[i] == 0) {
                dfs(M, visitied, i);
                res += 1;
            }
        }
        return res;
    }

    private void dfs(int[][] M, int[] visitied, int i) {
        for (int j = 0; j < M.length; j++) {
            if (M[i][j] == 1 && visitied[j] == 0) {
                visitied[j] = 1;
                dfs(M, visitied, j);
            }
        }
    }

    static boolean visited = false;

    //    [[1,2],[1,3],[2,4],[2,5],[4,8],[4,9],[3,6],[3,7]],8,6
    public static void main111(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = sc.next();
        int node1 = (int) str.charAt(str.length() - 1);
        int node2 = str.charAt(str.length() - 3);
        TreeNode nodeSource = null;
        TreeNode nodeTarget = null;
        String newstr = str.substring(1, str.length() - 5);
        TreeNode root = null;
        for (int i = 0; i < newstr.length(); i += 7) {
            if (root == null) {
                root = new TreeNode(newstr.charAt(i + 1));
                root.left = new TreeNode(newstr.charAt(i + 2));
                if (newstr.charAt(i + 3) == node1 || newstr.charAt(i + 2) == node2) {
                    nodeSource = root.left;
                }
            } else {
                TreeNode node = getRoot(root, newstr.charAt(i + 1));
                if (node.left == null) {
                    node.left = new TreeNode(newstr.charAt(i + 2));
                } else {
                    node.right = new TreeNode(newstr.charAt(i + 2));
                }
                if (newstr.charAt(i + 2) == node1 || newstr.charAt(i + 2) == node2) {
                    nodeSource = root.left;
                }
            }

        }
        TreeNode parent = getParent(root, nodeSource, nodeTarget);
        LinkedList<Integer> stack1 = new LinkedList<>();
        LinkedList<Integer> stack2 = new LinkedList<>();
        getDisToParent(parent, nodeSource, stack1);
        visited = false;
        getDisToParent(parent, nodeTarget, stack2);
        System.out.println(stack1.size() + stack2.size() - 2);

    }

    public static TreeNode getRoot(TreeNode root, int value) {
        if (root == null) {
            return null;
        }
        if (root.value == value) {
            return root;
        }
        if (root.left != null) {
            getRoot(root.left, value);
        }
        if (root.right != null) {
            getRoot(root.right, value);
        }
        return root;
    }

    public static TreeNode getParent(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) {
            return root;
        }
        TreeNode left = getParent(root.left, p, q);
        TreeNode right = getParent(root.right, p, q);
        if (left != null && right != null) {
            return root;
        }
        return left == null ? right : left;
    }

    public static void getDisToParent(TreeNode root, TreeNode p, LinkedList<Integer> stack) {
        if (root == null) {
            return;
        }
        stack.push(root.value);
        if (!visited && root == p) {
            visited = true;
            return;
        }
        if (!visited) {
            getDisToParent(root.left, p, stack);
        }
        if (!visited) {
            getDisToParent(root.right, p, stack);
        }
        if (!visited) {
            stack.pop();
        }
        return;
    }

    /**
     * 72229,in,20931|
     * 72229,in,20931|
     * 72229,in,20931|
     * 21257,out,68387|
     * 21257,out,68387|
     * 21257,in,68387|
     * 66362,in,31725|
     * 66362,in,31725|
     * 66362,in,3172
     */

    public static void main84(String[] args) {
        Scanner in = new Scanner(System.in);
        String str = in.next();
        List<String> list = Arrays.asList(str.split("|"));
        System.out.println(maxDiff1(list));
    }

    private static int maxDiff1(List<String> list) {
        int[] arr = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).contains("in")) {
                arr[i] = 1;
            } else if (list.get(i).contains("out")) {
                arr[i] = -1;
            }
        }
        int max = arr[0];
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
            if (sum < 0) {
                sum = 0;
            }
            if (sum > max) {
                max = sum;
            }
        }
        return max;
    }

    /**
     * user1,device1
     * user2,device2
     * user3,device1
     * user1
     * 0,1
     * 2,3
     * 4,1
     * 0
     */
    public static void main009(String[] args) {
        Scanner in = new Scanner(System.in);
        String str = in.next();
        List<String> list = Arrays.asList(str.split(","));
        List<String> list1 = new ArrayList<>();
        while (list.size() >= 2) {
            list1.add(list.get(0));
            list1.add(list.get(1));
            str = in.next();
            list = Arrays.asList(str.split(","));
        }
        int i = Integer.valueOf(list.get(0));
        int[][] M = new int[list1.size()][];

    }

    // 关键字：桶排序，什么数字就要放在对应的索引上，其它空着就空着
    // 最好的例子：[3,4,-1,1]
    // 整理好应该是这样：[1,-1,3,4]，
    // 这里 1，3，4 都在正确的位置上，
    // -1 不在正确的位置上，索引是 1 ，所以返回 2
    // [4,3,2,1] 要变成 [1,2,3,4]
    // 这里负数和大于数组长度的数都是"捣乱项"。
    public int firstMissingPositive(int[] nums) {
        if (nums == null || nums.length == 0) {
            return -1;
        }
        int len = nums.length;
        for (int i = 0; i < nums.length; i++) {
            while (i != nums[i] && nums[i] > 0 && nums[i] < len) {
                swap(nums, i, nums[i]);
            }
        }
        for (int i = 0; i < nums.length; i++) {
            if (i != nums[i]) {
                return i;
            }
        }
        return len + 1;
    }

    private void swap(int[] nums, int index1, int index2) {
        if (index1 == index2) {
            return;
        }
        int temp = nums[index1];
        nums[index1] = nums[index2];
        nums[index2] = temp;
    }

    public static void main(String[] args) {
        Test solution = new Test();
         int[] nums = {0, 4, -1, 5};
//        int[] nums = {4, 3, 2, 1};
        int firstMissingPositive = solution.firstMissingPositive(nums);
        System.out.println(firstMissingPositive);
    }
}

class LNode {
    //为了简化访问单链表,结点中的数据项的访问权限都设为public
    public int data;
    public LNode next;
}

class TreeNode {
    public int value;
    public TreeNode left;
    public TreeNode right;

    public TreeNode(int value) {
        this.value = value;
    }

    public TreeNode() {
    }
}

