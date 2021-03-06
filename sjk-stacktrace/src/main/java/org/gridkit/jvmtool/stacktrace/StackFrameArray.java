package org.gridkit.jvmtool.stacktrace;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class StackFrameArray implements StackFrameList {

    final StackFrame[] array;
    final int from;
    final int to;
    
    public StackFrameArray(StackFrame[] array) {
        this(array, 0, array.length);
    }

    public StackFrameArray(StackFrame[] array, int from, int to) {
        this.array = array;
        this.from = from;
        this.to = to;
    }

    @Override
    public Iterator<StackFrame> iterator() {
        return new Iterator<StackFrame>() {
            int n = from;

            @Override
            public boolean hasNext() {
                return n < to;
            }

            @Override
            public StackFrame next() {
                if (n >= to) {
                    throw new NoSuchElementException();
                }
                return array[n++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public StackFrame frameAt(int n) {
        if (n < 0) {
            throw new IndexOutOfBoundsException(n + " < 0");
        }
        int nn = from + n;
        if (nn >= to) {
            throw new IndexOutOfBoundsException(nn + " > [" + from + "," + to + ")");
        }
        return array[nn];
    }

    @Override
    public int depth() {
        return to - from;
    }

    @Override
    public StackFrameList fragment(int from, int to) {
        if (from > to) {
            throw new IllegalArgumentException();
        }
        int nfrom = this.from + from;
        int nto = this.from + to;
        if (nfrom >= this.to || nfrom < this.from) {
            throw new IndexOutOfBoundsException(nfrom + " > [" + this.from + "," + this.to + ")");
        }
        if (nto > this.to || nto < this.from) {
            throw new IndexOutOfBoundsException(nto + " > [" + this.from + "," + this.to + "]");
        }
        return new StackFrameArray(array, nfrom, nto);
    }

    @Override
    public String toString() {
        return Arrays.asList(array).subList(from, to).toString();
    }
}
