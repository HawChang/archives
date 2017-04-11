#include <iostream>
#include <windows.h>
#include "avlTree.h"
using namespace std;

int main()
{
    int choose;
    avlTree<char> test;
    char input;
    cout<<"-------------------------------------------------------------------------------"<<endl;
    cout<<"--                    choose functions:                                      --"<<endl;
    cout<<"--        1.insrt  --2.delete  --3.display  --4.consult  --others.exit       --"<<endl;
    cout<<"-------------------------------------------------------------------------------"<<endl;
    while(cin>>choose)
    {
        switch(choose)
        {
        case 1:
            cout<<"input the charactor you want to insert(input '#' to esc):"<<endl;
            cout<<"-----------------------------------------------------------------------------"<<endl<<">>";
            while(cin>>input)
            {
                switch(input)
                {
                case '#':
                    system("cls");
                    goto exit_input;
                    break;
                default:
                    test.insert(input);
                    cout<<"display in inorder:";
                    test.inOrderTraversal();
                    cout<<endl;
                    break;
                }
                cout<<"input the charactor you want to insert(input '#' to esc):"<<endl;
                cout<<"-----------------------------------------------------------------------------"<<endl<<">>";
            }
            break;
        case 2:
            cout<<"input the charactor you want to delete(never input the charactor not exist):"<<endl<<">>";
            cin>>input;
            test.deletex(input);
            cout<<"display in inorder:";
            test.inOrderTraversal();
            cout<<endl;
            break;
        case 3:
            cout<<"display in inorder:";
            test.inOrderTraversal();
            cout<<endl;
            break;
        case 4:
            cout<<"input the charactor you want to consult:"<<endl<<">>";
            cin>>input;
            test.consult(input);
            cout<<"display in inorder:";
            test.inOrderTraversal();
            cout<<endl;
            break;
        default:
            return 0;
        }
exit_input:
        cout<<"-------------------------------------------------------------------------------"<<endl;
        cout<<"--                    choose functions:                                      --"<<endl;
        cout<<"--        1.insrt  --2.delete  --3.display  --4.consult  --others.exit       --"<<endl;
        cout<<"-------------------------------------------------------------------------------"<<endl;
    }
    return 0;
}
