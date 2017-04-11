#include<iostream>
#include <windows.h>
#include "links_tree.h"
using namespace std;
//1 2 3 0 0 4 0 5 0 0 6 0 0
int main()
{
    int choose;
    BiThreTree<char> *T, *Thre;
    do
    {
        system("cls");
        cout<<"initialize the tree, input the tree in preorder:"<<endl;
        cout<<"('.' represents no lchild or no rchild.)"<<endl<<">>";
        T=T->CreateTree();
        Thre=T->InOrderThrTree();
        cout<<"traverse the links tree in inorder."<<endl;
        Thre->InThrTravel();
        cout<<endl;
        cout<<"traverse the links tree in preorder."<<endl;
        Thre->FirstThrTravel();
        cout<<endl;
        cout<<"traverse the links tree in postorder."<<endl;
        Thre->LastThrTravel();
        cout<<endl;
        cout<<"press any key to continue, '0' to esc."<<endl<<">>";
    }
    while(cin>>choose&&choose!=0);

    return 0;
}
