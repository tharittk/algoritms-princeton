public class CircularSuffixArray {
	public CircularSuffixArray(String s){
		if(s == null) throw new IllegalArgumetException("null string input !");
		
		CircularSuffix[] csArray = new CircurlarSuffix[s.length()];
		
		for (int i = 0; i < s.length(); i++){
			csArray[i] = new CircularSuffix(i, s);
		}	
		Arrays.sort(csArray);
	}

	private class CircularSuffix implements Comparable<CircularSuffixArray<T>>{
		int final pos;
		int offset;
		String final s;

		private CircularSuffix (int pos, String sref){
			this.pos = pos;
			this.offset = 0;
			this.s = sref;
		}
		
		private char getCharAt(int i){
			return s.charAt((pos + i) % s.length());
		}


		private int compareTo(CircularSuffix other){
			
			if (this.offset == s.length() && other.offset == s.length()){
				// exactly same suffix
				this.offset = 0;
				other.offset = 0;
				return 0;
			}

			int cmp = Character.compare( this.s.charAt[this.offset % s.length()] , other.s.charAt[other.offset % s.length()]);
		
			if (cmp == 0){
				//recursively compare next character
				this.offset++;
				other.offset++;
				return compareTo(other);
			} else {
				this.offset = 0;
				other.offset = 0;
				return cmp;
			}
		}	
	}		
	
	public int length(){
		return s.length();
	}

	public int index(int i){
		if (i < 0 || i >= this.length()) throw new IllegalArgumentException("i must be between 0 and n-1");
		return csArray[i].pos;
	}

	public static void main(String[] args){
	}

}
