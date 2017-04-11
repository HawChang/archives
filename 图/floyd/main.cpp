#include <iostream>
#include "floyd.h"
using namespace std;

int main()
{
    char a,b;
    floyd<char> f;
    f.create();
    f.floyd_help();
//    cout<<"find"<<endl;
//    cin>>a>>b;
//    f.find_path(a,b);
    return 0;
}
