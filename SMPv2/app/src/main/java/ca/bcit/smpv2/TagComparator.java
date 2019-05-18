package ca.bcit.smpv2;

import java.util.Comparator;

public class TagComparator implements Comparator<Tag> {
    @Override
    public int compare(Tag o1, Tag o2) {
        return (o1.getBusinessID() == o2.getBusinessID() && o1.getTag() == o2.getTag()) ? 1 : 0;
    }
}