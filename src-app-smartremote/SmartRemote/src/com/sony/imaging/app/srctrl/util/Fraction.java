package com.sony.imaging.app.srctrl.util;

import java.util.Comparator;

import android.util.Pair;

public class Fraction
{
    private long numerator;
    private long denominator;
    
    public static class FractionComparator implements Comparator<Fraction>
    {
        @Override
        public int compare(Fraction arg0, Fraction arg1)
        {
            return arg0.compare(arg1);
        }
    }
    
    public static final FractionComparator COMPARATOR = new FractionComparator();
    
    @SuppressWarnings("unused")
    private Fraction()
    {
    }
    
    public Fraction(String source)
    {
        int pos = source.indexOf('\"');
        if (-1 != pos)
        {
            source = source.substring(0, pos);
        }
        
        pos = source.indexOf('.');
        if (-1 != pos)
        {
            String decimal = source.substring(pos + 1, source.length());
            float f = Float.parseFloat(source);
            denominator = (long) Math.pow(10, decimal.length());
            numerator = (long) (f * denominator);
            return;
        }
        
        pos = source.indexOf("/");
        if (-1 != pos)
        {
            String positive = source.substring(0, pos);
            String decimal = source.substring(pos + 1, source.length());
            numerator = Long.parseLong(positive);
            denominator = Long.parseLong(decimal);
            return;
        }
        
        numerator = Long.parseLong(source);
        denominator = 1L;
    }
    
    public Fraction(Pair<Integer, Integer> source) throws ArithmeticException
    {
        this(source.first.intValue(), source.second.intValue());
    }
    
    public Fraction(int numerator, int denominator) throws ArithmeticException
    {
        if (0L == denominator)
        {
            throw new java.lang.ArithmeticException();
        }
        this.numerator = numerator;
        this.denominator = denominator;
    }
    
    public Fraction(Fraction source) throws ArithmeticException
    {
        numerator = source.numerator;
        denominator = source.denominator;
        
        if (0L == denominator)
        {
            throw new java.lang.ArithmeticException();
        }
    }
    
    /**
     * Compare two fractions
     * 
     * @param r
     *            a fraction to be compared
     * @return 1 if this fraction is larger than another. -1 if this fraction is
     *         smaller than another. 0 if two fractions are the same.
     */
    public int compare(Fraction ref)
    {
        Fraction l = this.clone();
        Fraction r = ref.clone();
        if (l.denominator > r.denominator)
        {
            if (0 == (l.denominator % r.denominator))
            {
                r.numerator = r.numerator * l.denominator / r.denominator;
                r.denominator = l.denominator;
            }
            else
            {
                long l_numerator = l.numerator * r.denominator;
                long l_denominator = l.denominator * r.denominator;
                long r_numerator = r.numerator * l.denominator;
                long r_denominator = r.denominator * l.denominator;
                l.numerator = l_numerator;
                l.denominator = l_denominator;
                r.numerator = r_numerator;
                r.denominator = r_denominator;
            }
        }
        if (l.denominator < r.denominator)
        {
            if (0 == (r.denominator % l.denominator))
            {
                l.numerator = l.numerator * r.denominator / l.denominator;
                l.denominator = r.denominator;
            }
            else
            {
                long l_numerator = l.numerator * r.denominator;
                long l_denominator = l.denominator * r.denominator;
                long r_numerator = r.numerator * l.denominator;
                long r_denominator = r.denominator * l.denominator;
                l.numerator = l_numerator;
                l.denominator = l_denominator;
                r.numerator = r_numerator;
                r.denominator = r_denominator;
            }
        }
        
        if (l.numerator == r.numerator)
        {
            return 0;
        }
        else if (l.numerator > r.numerator)
        {
            return 1;
        }
        else
        {
            return -1;
        }
    }
    
    @Override
    public String toString()
    {
        return numerator + "/" + denominator;
    }
    
    @Override
    protected Fraction clone()
    {
        return new Fraction(this);
    }
}
