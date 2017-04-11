#include "binary_tree.h"

#include <iostream>
using namespace std;

int main()
{
    int choose,number;
    btree<char> bt;
    do
    {
        cout<<"initialize the tree first:"<<endl;
        cout<<"input the head of the tree"<<endl<<">>";
        bt.create();
        cout<<endl;
        cout<<"traverse the tree in preorder:"<<endl;
        bt.preorder();
        cout<<endl;
        cout<<"traverse the tree in postorder:"<<endl;
        bt.postorder();
        cout<<endl;
        cout<<"traverse the tree in inorder:"<<endl;
        bt.inorder();
        cout<<endl;
        cout<<"-------------------------------------------------------------------------------"<<endl;
        cout<<"-------input any key to create new tree or input \"0\" to esc  ------------------"<<endl;
        cout<<"-------------------------------------------------------------------------------"<<endl;
    }
    while(cin>>choose&&cin!=0);
    return 0;
}
