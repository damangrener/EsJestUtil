package com.wtf.esjest.esjestutil.page;

import java.io.Serializable;

public class Pagination extends RowBounds implements Serializable {
    private static final long serialVersionUID = 1L;
    private long total;
    private int size;
    private int current;

    public Pagination() {
        super();
        this.current = 1;
        this.size = super.getLimit();
    }

    public Pagination(int current, int size) {
        super(PageHelper.offsetCurrent(current, size) , size);
        if (current > 1) {
            this.current = current;
        } else{
            this.current = 1;
        }
        this.size = size;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public int getCurrent() {
        return current;
    }

    public long getPages() {
        if (this.size == 0) {
            return 0L;
        } else {
            long pages = this.total / (long)this.size;
            if (this.total % (long)this.size != 0L) {
                ++pages;
            }

            return pages;
        }
    }

    public String toString() {
        return "Pagination { total=" + this.total + " ,size=" + this.size + " ,pages=" + this.getPages() + " ,current=" + this.current + " }";
    }
}
