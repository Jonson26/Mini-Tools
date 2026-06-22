import java.util.Scanner;
public class bfi{
	public static void main(String[]a) {
		Scanner sc = new Scanner(System.in);
		for(var P:a){
			int[]memory=new int[256];
			int[]stack=new int[256];
			int i,j,k;i=j=k=0;
			while(k<P.length()){
				char c = P.charAt(k);k++;
				switch(c){
					case'>':i++;break;
					case'<':i--;break;
					case'+':memory[i]++;break;
					case'-':memory[i]--;break;
					case'.':System.out.print(memory[i]);break;
					case',':System.out.print("\n?");memory[i]=sc.nextInt();break;
					case'[':
						if(memory[i]>0){
							stack[j]=k-1;j++;
						}else{
							int l=1;
							while(l>0){
								k++;char d = P.charAt(k);
								if(d==']')l--;
								if(d=='[')l++;
							}
							k++;
						}
						break;
					case']':j--;k=stack[j];break;
				}
			}
		}
	}
}