public class BurrowsWheeler {
	public static void transform(){
		String s = BinaryStdIn.readString();
		CircularSuffixArray csa = CircularSuffixArray(s);			
		StringBuilder t = new StringBuilder();
		int first;

		for(int i = 0; i < csa.lengt(); i++){
			int idxi = csa.index(i);
			if(idxi == 0){
				t.append(s.charAt(csa.length() - 1));
				first = i;
			} else {
				t.append(s.charAt(idxi - 1));
			}
	
		}
		// stdout
		BinaryStdOut.write(first);
		BinaryStdOut.write(t);
	}

	public static void inverseTransform(){
		int first = BinaryStdIn.readInt();
		String t = BinaryStdIn.readString();
		
		char[] tSorted = new char[t.length];

		// building next
		int R = 256; // extended ASCII
		int N = t.length;
		int[] count = new int[R+i];
		int[] next = new int[N];

		List<Integer>[] charPositionLists = new ArrayList[R+1];
		// count frequency and store the list of index in t where char is found
		for (int i = 0; i < N; i++){
				int c = t.charAt(i);
				count[c+1]++;
				charPositionLists[c+1].add(i);
		}
		
		for (int r = 0; r < R; r++){
			count[r+1] += count[r];
		}

		for (int i = 0; i < N; i++){
			tSorted[count[t.charAt(i)]++] = t.charAt(i);
		}

		// building next
		int i = 0;
		for (List<Integer> positions : charPositionLists){ 
			for (int position : positions){
				next[i] = position;
				i++;	
			}
		}

		// inverting
		StringBuilder it = new StringBuilder();
		int i = first;
		while (next[i] != first){
			it.append(tSorted[i]);
			i = next[i];
		}
		BinaryStdOut.write(it);
	}


	public static void main(String[] args)


}

