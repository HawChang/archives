#include <iostream>
#include "btree.h"
using namespace std;

int main()
{
    int choose;
    btree<char> bt;
    do
    {
        cout<<"initialize the tree first:"<<endl;
        cout<<"input the head of the tree"<<endl<<">>";
        bt.create();
        cout<<endl;
        cout<<"traverse the tree in preorder by recursion:"<<endl;
        bt.preorder();
        cout<<endl;
        cout<<"traverse the tree in preorder:"<<endl;
        bt.nonre_preorder();
        cout<<endl;
        cout<<"traverse the tree in postorder by recursion:"<<endl;
        bt.postorder();
        cout<<endl;
        cout<<"traverse the tree in postorder:"<<endl;
        bt.postorder();
        cout<<endl;
        cout<<"traverse the tree in inorder by recursion:"<<endl;
        bt.inorder();
        cout<<endl;
        cout<<"traverse the tree in inorder:"<<endl;
        bt.nonre_inorder();
        cout<<endl;
        cout<<"-------------------------------------------------------------------------------"<<endl;
        cout<<"-------input any key to create new tree or input \"0\" to esc  ------------------"<<endl;
        cout<<"-------------------------------------------------------------------------------"<<endl;
    }
    while(cin>>choose&&choose!=0);
    return 0;
}
