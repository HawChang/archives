#include <iostream>
#include "sort.h"
using namespace std;

int main()
{
    fast_sort a;
    a.bubble_sort();
    a.insert_sort();
    a.select_sort();
    a.quick_sort();
    a.merge_sort();
    a.shell_sort();
    a.radix_sort();
    return 0;
}
