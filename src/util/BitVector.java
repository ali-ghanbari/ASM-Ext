package util;

/**
 * A fast implementation of bit vector
 * 
 * @author Ali Ghanbari
 *
 */

public class BitVector {
	private static final int CHUNK_SIZE = Long.SIZE;
	
	private long[] bitmap;
	
	private int size;
	
	private BitVector(int size, long[] bitmap) {
		this.bitmap = bitmap;
		this.size = size;
	}

	public BitVector() {
		size = 0;
		bitmap = new long[0];
	}
	
	private static long assign(long chunk, int index, boolean value) {
		final long marker = 1L << (CHUNK_SIZE - index - 1);
		return value ? chunk | marker : chunk & ~marker;
	}
	
	public void add(boolean value) {
		if((size % CHUNK_SIZE) == 0) {
			long[] bitmap_ext = new long[bitmap.length + 1];
			System.arraycopy(bitmap, 0, bitmap_ext, 0, bitmap.length);
			bitmap_ext[bitmap.length] = assign(0, 0, value);
			bitmap = bitmap_ext;
		} else {
			bitmap[bitmap.length - 1] = assign(bitmap[bitmap.length - 1],
					size % CHUNK_SIZE, value);
		}
		size++;
	}
	
	public void set(int index) {
		assert(0 <= index && index < size);
		final int chunk_index = index / CHUNK_SIZE;
		bitmap[chunk_index] = assign(bitmap[chunk_index], index % CHUNK_SIZE, true);
	}
	
	public void reset(int index) {
		assert(0 <= index && index < size);
		final int chunk_index = index / CHUNK_SIZE;
		bitmap[chunk_index] = assign(bitmap[chunk_index], index % CHUNK_SIZE, false);
	}
	
	public boolean get(int index) {
		assert(0 <= index && index < size);
		final long chunk = bitmap[index / CHUNK_SIZE];
		final long probe = 1L << (CHUNK_SIZE - (index % CHUNK_SIZE) - 1);
		return (chunk & probe) != 0;
	}
	
	public int size() {
		return size;
	}
	
	public static BitVector and(BitVector op1, BitVector op2) {
		assert(op1.size == op2.size);
		int size = op1.size;
		long[] bitmap = new long[op1.bitmap.length];
		for(int i = 0; i < op1.bitmap.length; i++) {
			bitmap[i] = op1.bitmap[i] & op2.bitmap[i];
		}
		return new BitVector(size, bitmap);
	}
	
	public BitVector and(BitVector other) {
		assert(size == other.size);
		for(int i = 0; i < bitmap.length; i++) {
			bitmap[i] &= other.bitmap[i];
		}
		return this;
	}
	
	public static BitVector or(BitVector op1, BitVector op2) {
		assert(op1.size == op2.size);
		int size = op1.size;
		long[] bitmap = new long[op1.bitmap.length];
		for(int i = 0; i < op1.bitmap.length; i++) {
			bitmap[i] = op1.bitmap[i] | op2.bitmap[i];
		}
		return new BitVector(size, bitmap);
	}
	
	public BitVector or(BitVector other) {
		assert(size == other.size);
		for(int i = 0; i < bitmap.length; i++) {
			bitmap[i] |= other.bitmap[i];
		}
		return this;
	}
	
	public static BitVector negate(BitVector op) {
		int size = op.size;
		long[] bitmap = new long[op.bitmap.length];
		for(int i = 0; i < op.bitmap.length; i++) {
			bitmap[i] = ~op.bitmap[i];
		}
		return new BitVector(size, bitmap);
	}
	
	public BitVector negate() {
		for(int i = 0; i < bitmap.length; i++) {
			bitmap[i] = ~bitmap[i];
		}
		return this;
	}
	
	private static String toBinaryString(long chunk, int base) {
		String result = "";
		for(int i = 0; i < CHUNK_SIZE; i++) {
			if(i >= base) {
				result = (chunk & 1) + result;
			}
			chunk >>>= 1;
		}
		return result;
	}
	
	@Override
	public String toString() {
		String result = "";
		for(int chunk_index = 0; chunk_index < bitmap.length - 1; chunk_index++) {
			result += toBinaryString(bitmap[chunk_index], 0);
		}
		if(bitmap.length > 0) {
			result += toBinaryString(bitmap[bitmap.length - 1], 
					bitmap.length * CHUNK_SIZE - size);
		}
		return result;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof BitVector))
			return false;
		BitVector other = (BitVector) o;
		if(size != other.size)
			return false;
		for(int i = 0; i < bitmap.length; i++) {
			if(bitmap[i] != other.bitmap[i]) {
				return false;
			}
		}
		return true;
	}

}
