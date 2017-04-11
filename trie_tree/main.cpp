#include <iostream>
#include <conio.h>
#include "trie_tree.h"
using namespace std;
int main()
{
//    char key;
//    key=getch();
//    if(key>128)key=getch();
//    if(key==0) key=getch()+400;
//    cout<<"No."<<key;
    trie_tree t;
    t.create();
    //t.display();
    t.consult();
    return 0;
}
