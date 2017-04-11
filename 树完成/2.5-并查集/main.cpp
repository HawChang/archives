#include "union_search.h"
#include <iostream>
#include <windows.h>
using namespace std;



int main()
{
    int choose;
    uni<char> h;
    do
    {
        system("cls");
        h.create();
        h.trans();
        h.display();
        cout<<"press any key to continue or '0' to esc."<<endl;
    }
    while(cin>>choose&&choose!=0);
    return 0;
}
