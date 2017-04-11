#include <iostream>
#include <windows.h>
#include "openhash.h"

void menu()
{
    cout<<"------------------choose functions----------------------------------"<<endl;
    cout<<"---1.insrt   2.search   3.display   4.initialize   osthers.exit-----"<<endl;
}
int main()
{
    int choose,n;
    OpenHash o;
    o.Init();
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
                system("cls");
                o.Insert(n);
                o.display();
                cout<<"press any number to insert,'-1' to esc."<<endl;
            }
            break;
        case 2:
            cout<<"press any number to search,'-1' to esc."<<endl;
            while(cin>>n&&n!=-1)
            {
                system("cls");
                if(o.Search(n));
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
            o.Init();
            cout<<"initialize succeed..."<<endl;
            o.display();
            break;
        }
        menu();
    }
    return 0;
}
