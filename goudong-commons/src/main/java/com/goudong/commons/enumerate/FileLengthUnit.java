package com.goudong.commons.enumerate;

/**
 * 枚举描述：
 * 文件大小单位，模仿 TimeUnit
 *
 * @see java.util.concurrent.TimeUnit
 * @author msi
 * @version 1.0
 * @date 2021/12/4 22:29
 */
public enum FileLengthUnit {
    BIT{
        public long toBits(long d)   { return d; }
        public long toBytes(long d)   { return d/C1; }
        public long toKbs(long d)   { return d/C2; }
        public long toMbs(long d)   { return d/C3; }
        public long toGbs(long d)   { return d/C4; }
        public long toTbs(long d)   { return d/C5; }
        public long convert(long d, FileLengthUnit u) { return u.toBits(d); }
    },
    BYTE{
        public long toBits(long d)   { return d*C1; }
        public long toBytes(long d)   { return d; }
        public long toKbs(long d)   { return d/(C2/C1); }
        public long toMbs(long d)   { return d/(C3/C1); }
        public long toGbs(long d)   { return d/(C4/C1); }
        public long toTbs(long d)   { return d/(C5/C1); }
        public long convert(long d, FileLengthUnit u) { return u.toBytes(d); }
    },
    KB{
        public long toBits(long d)   { return d*C2; }
        public long toBytes(long d)   { return d*(C2/C1); }
        public long toKbs(long d)   { return d; }
        public long toMbs(long d)   { return d/(C3/C2); }
        public long toGbs(long d)   { return d/(C4/C2); }
        public long toTbs(long d)   { return d/(C5/C2); }
        public long convert(long d, FileLengthUnit u) { return u.toKbs(d); }
    },
    MB{
        public long toBits(long d)   { return d*C3; }
        public long toBytes(long d)   { return d*(C3/C1); }
        public long toKbs(long d)   { return d*(C3/C2); }
        public long toMbs(long d)   { return d; }
        public long toGbs(long d)   { return d/(C4/C3); }
        public long toTbs(long d)   { return d/(C5/C3); }
        public long convert(long d, FileLengthUnit u) { return u.toMbs(d); }
    },
    GB{
        public long toBits(long d)   { return d*C4; }
        public long toBytes(long d)   { return d*(C4/C1); }
        public long toKbs(long d)   { return d*(C4/C2); }
        public long toMbs(long d)   { return d/(C4/C3); }
        public long toGbs(long d)   { return d; }
        public long toTbs(long d)   { return d/(C5/C4); }
        public long convert(long d, FileLengthUnit u) { return u.toGbs(d); }
    },
    TB{
        public long toBits(long d)   { return d*C5; }
        public long toBytes(long d)   { return d*(C5/C1); }
        public long toKbs(long d)   { return d*(C5/C2); }
        public long toMbs(long d)   { return d/(C5/C3); }
        public long toGbs(long d)   { return d/(C5/C4); }
        public long toTbs(long d)   { return d; }
        public long convert(long d, FileLengthUnit u) { return u.toTbs(d); }
    };

    // Handy constants for conversion methods
    static final long C0 = 1L;
    static final long C1 = C0 * 8L;
    static final long C2 = C1 * 1024L;
    static final long C3 = C2 * 1024L;
    static final long C4 = C3 * 1024L;
    static final long C5 = C4 * 1024L;

    static final long MAX = Long.MAX_VALUE;

    static long x(long d, long m, long over) {
        if (d >  over) return Long.MAX_VALUE;
        if (d < -over) return Long.MIN_VALUE;
        return d * m;
    }
    /**
     * 将给定单位的给定大小转换为此单位。 从更细粒度到更粗粒度的转换会被截断，因此会失去精度。
     * 例如，将1023Byte转换为KB结果为0 。 从粗粒度到细粒度的转换，如果为负，则参数会在数值上溢出饱和Long.MIN_VALUE如果为正， Long.MAX_VALUE 。
     * 例如，要将 10 KB转换为BYTE，请使用： FileLengthUnit.KB.convert(10L, FileLengthUnit.BYTE)
     * @param sourceLength 给定sourceUnit的长度
     * @param sourceUnit sourceLength参数的单位
     * @return 此单位中的转换length，如果转换会负溢出， Long.MIN_VALUE如果转换为正溢出， Long.MAX_VALUE 。
     */
    public long convert(long sourceLength, FileLengthUnit sourceUnit) {
        throw new AbstractMethodError();
    }

    /**
     * 相当于Bit.convert(length, this) 。
     * @param length 转换前的大小
     * @return 转换后的大小，或者Long.MIN_VALUE如果转换会负溢出，或者Long.MAX_VALUE如果它会正溢出。
     */
    public long toBits(long length) {
        throw new AbstractMethodError();
    }

    /**
     * 相当于BYTE.convert(length, this) 。
     * @param length 转换前的大小
     * @return 转换后的大小，或者Long.MIN_VALUE如果转换会负溢出，或者Long.MAX_VALUE如果它会正溢出。
     */
    public long toBytes(long length) {
        throw new AbstractMethodError();
    }

    /**
     * 相当于 KB.convert(length, this) 。
     * @param length 转换前的大小
     * @return 转换后的大小，或者Long.MIN_VALUE如果转换会负溢出，或者Long.MAX_VALUE如果它会正溢出。
     */
    public long toKbs(long length) {
        throw new AbstractMethodError();
    }

    /**
     * 相当于 MB.convert(length, this) 。
     * @param length 转换前的大小
     * @return 转换后的大小，或者Long.MIN_VALUE如果转换会负溢出，或者Long.MAX_VALUE如果它会正溢出。
     */
    public long toMbs(long length) {
        throw new AbstractMethodError();
    }

    /**
     * 相当于 GB.convert(length, this) 。
     * @param length 转换前的大小
     * @return 转换后的大小，或者Long.MIN_VALUE如果转换会负溢出，或者Long.MAX_VALUE如果它会正溢出。
     */
    public long toGbs(long length) {
        throw new AbstractMethodError();
    }

    /**
     * 相当于 TB.convert(length, this) 。
     * @param length 转换前的大小
     * @return 转换后的大小，或者Long.MIN_VALUE如果转换会负溢出，或者Long.MAX_VALUE如果它会正溢出。
     */
    public long toTbs(long length) {
        throw new AbstractMethodError();
    }
}
