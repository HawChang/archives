#include <iostream>
#include <windows.h>
#include "shortest.h"
using namespace std;
void menu()
{
    cout<<"-------------------------------choose functions--------------------------------"<<endl;
    cout<<"--  1.dfs_search         2.bfs_search         3.dijkstra        4.topology  ---"<<endl;
    cout<<"--  5.special road                 6.display                    others.exit ---"<<endl;
    cout<<"-------------------------------------------------------------------------------"<<endl;
}
int main()
{
    int choose;
    char s;
    adjgraph<char> adj;
    adj.create();
//    cout<<"dfs from apex:"<<endl<<">>";
//    cin>>s;
//    adj.dfs_search(s);
//    adj.bfs_search(s);
//    adj.dijkstra(s);
//    adj.topology();
//    adj.special_road();
    menu();
    while(cin>>choose)
    {
        switch(choose)
        {
        case 1:
            system("cls");
            menu();
            cout<<"dfs from apex:"<<endl<<">>";
            cin>>s;
            adj.dfs_search(s);
            break;
        case 2:
            system("cls");
            menu();
            cout<<"bfs from apex:"<<endl<<">>";
            cin>>s;
            adj.bfs_search(s);
            break;
        case 3:
            system("cls");
            menu();
            cout<<"dijsktra from apex:"<<endl<<">>";
            cin>>s;
            adj.dijkstra(s);
            break;
        case 4:
            system("cls");
            menu();
            adj.topology();
            break;
        case 5:
            system("cls");
            menu();
            adj.special_road();
            break;
        case 6:
            system("cls");
            menu();
            adj.watch();
            break;
        default:
            return 0;
        }
    }

    cout << "Hello world!" << endl;
    return 0;
}
