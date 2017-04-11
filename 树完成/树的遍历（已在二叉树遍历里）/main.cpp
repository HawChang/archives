#include <iostream>
#include <windows.h>
#include "btree.h"
int main()
{
    int choose;
    btree<char> bt;
    cout<<"Initialize the Btree:"<<endl<<"input the head of the tree"<<endl<<">>";
    bt.create();
    cout<<"traverse the tree in:"<<endl;
    cout<<"---1.preorder   ---2.inorder   ---3.postorder"<<endl;
    cout<<"---4.clear   ---5.recreate"<<endl;
    while(cin>>choose)
    {
        system("cls");
        bt.Print(&bt,0);
        cout<<"traverse the tree in:"<<endl;
        cout<<"---1.preorder   ---2.inorder   ---3.postorder"<<endl;
        cout<<"---4.clear   ---5.recreate"<<endl;
        switch(choose)
        {
        case 1:
            cout<<"preorder:";
            bt.preorder();
            cout<<endl;
            break;
        case 2:
            cout<<"inorder:";
            bt.inorder();
            cout<<endl;
            break;
        case 3:
            cout<<"postorder:";
            bt.postorder();
            cout<<endl;
            break;
        case 4:
            bt.makenull();
            break;
        case 5:
            cout<<"Initialize the Btree:"<<endl<<"input the head of the tree"<<endl<<">>";
            bt.create();
            break;
        default:
            return 0;
        }
        cout<<"choose functions>>";
    }
    return 0;
}
