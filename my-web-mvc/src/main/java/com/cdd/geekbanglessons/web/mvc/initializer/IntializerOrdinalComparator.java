package com.cdd.geekbanglessons.web.mvc.initializer;

import com.cdd.geekbanglessons.configuration.microprofile.config.source.ConfigSourceOrdinalComparator;

import java.util.Comparator;

/**
 * @author yangfengshan
 * @create 2021-03-24 14:10
 **/
public class IntializerOrdinalComparator implements Comparator<AbstractMyWebMvcInitializer> {
    /**
     * Singleton instance {@link ConfigSourceOrdinalComparator}
     */
    public static final Comparator<AbstractMyWebMvcInitializer> INSTANCE = new IntializerOrdinalComparator();

    private IntializerOrdinalComparator() {
    }

    @Override
    public int compare(AbstractMyWebMvcInitializer o1, AbstractMyWebMvcInitializer o2) {
        return Integer.compare(o1.getOrdinal(), o2.getOrdinal());
    }
}
