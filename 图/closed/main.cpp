#include <iostream>
#include <windows.h>
#include "closed.h"
using namespace std;

void menu()
{
    cout<<"------------------choose functions----------------------------------"<<endl;
    cout<<"---1.insrt   2.search   3.display   4.initialize   osthers.exit-----"<<endl;
}
int main()
{
    int choose,n;
    closed<int> o;
    system("cls");
    o.display();
    menu();
    while(cin>>choose)
    {
        switch(choose)
        {
        case 1:
            cout<<"press any number to insert,'-1' to esc."<<endl;
            while(cin>>n&&n!=-1)
            {
                cout<<"wait"<<endl;
                system("cls");
                o.insrt(n);
                o.display();
                cout<<"press any number to insert,'-1' to esc."<<endl;
            }
            break;
        case 2:
            cout<<"press any number to search,'-1' to esc."<<endl;
            while(cin>>n&&n!=-1)
            {
                system("cls");
                if(o.found(n));
                else cout<<"the data doesn't exist."<<endl;
                o.display();
                cout<<"press any number to search,'-1' to esc."<<endl;
            }
            break;
        case 3:
            system("cls");
            o.display();
            break;
        case 4:
            cout<<"initialize the openhash..."<<endl;
            o.init();
             cout<<"initialize succeed..."<<endl;
             system("cls");
           o.display();
            break;
        default:
            return 0;
        }
        menu();
    }
    return 0;
}
