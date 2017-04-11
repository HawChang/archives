#include <iostream>
#include <windows.h>
#include "tree_to_btree.h"
using namespace std;
void menu()
{
    cout<<"-------------------------------------------------------------------------------"<<endl;
    cout<<"--                           choose functions:                               --"<<endl;
    cout<<"--      1.create normal tree                   2.trans into btree            --"<<endl;
    cout<<"-------------------------------------------------------------------------------"<<endl;
}
int main()
{
    int choose;
    adjtable<char> tree;
    menu();
    while(cin>>choose)
    {
        switch(choose)
        {
        case 1:
            system("cls");
            menu();
            tree.create();
            system("cls");
            menu();
            cout<<"create succeed!!!"<<endl;
            tree.display();
            break;
        case 2:
            tree.trans();
            system("cls");
            menu();
            cout<<"transform succeed!!!"<<endl;
            tree.display();
            break;
        default:
            return 0;
        }
    }
    return 0;
}
