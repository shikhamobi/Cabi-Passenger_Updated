package com.cabipassenger.calendercustom;

public interface DataChanged {
void intializeData(int month, int year);
void singleSelected(String Date);
void multiSelected(String DaysToBeHighlighted);
}
