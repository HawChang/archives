#include <iostream>
#include <cstdio>
#include "openAddress.h"
using namespace std;

int main()
{
    //for input  7
    //freopen("in.txt","r",stdin);
    hashTable test;
    test.insert(100);
    test.insert(20);
    test.insert(21);
    test.insert(35);
    test.insert(3);
    test.insert(78);
    test.insert(99);
    test.deletex(3);
    test.insert(10);
    test.print();
    return 0;
}
