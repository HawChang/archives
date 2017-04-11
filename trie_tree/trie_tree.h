#ifndef TRIE_TREE_H_INCLUDED
#define TRIE_TREE_H_INCLUDED
#include <string>
#include <iostream>
#include <windows.h>
#include <conio.h>
#include <fstream>
#define max_number 10000
#define esc 27
#define enter 13
#define backspace 8
#define print_number 14
using namespace std;
class trie_tree
{
private:
    typedef struct node
    {
        struct node* fchild;
        struct node* next_brother;
        char data;
        bool is_word;
        string explain;
    } node;
    node* head;
    int num;
    //int select_number;
    node* insrt_child(node* f,char key);
    node* child_search(node* f,char key) const;
    void display_help(node *h,node* stck[],int top) const;
    void display_help(node *h,node* stck[],int top,int & select_number) const;
    void select_help(string s);
    void menu();
    char select();
public:
    trie_tree()
    {
        num=0;
        head=new node;
        head->fchild=NULL;
        head->next_brother=NULL;
        head->is_word=false;
    }
    ~trie_tree()
    {
        delete head;
    }
    void insrt(string s,string e);
    void display() const;
    void create();
    void consult();
};
void trie_tree::create()
{
    string s,e;
    ifstream infile;
    infile.open("dictionary.txt",ios::out);
    if(infile.fail())
    {
        cout<<"doesn't find the dictionary."<<endl;
        return;
    }
    while(!infile.eof())
    {
        getline(infile,s);
        getline(infile,e);
        insrt(s,e);
    }
    infile.close();
}
void trie_tree::display_help(node* h,node* stck[],int top,int & select_number) const
{
    if(h&&select_number<=print_number)
    {
        stck[top++]=h;
        if(h->is_word)
        {
            select_number++;
            for(int i=0; i<top; i++)
            {
                cout<<stck[i]->data;
            }
            cout<<":";
            cout<<h->explain<<endl;
        }
        display_help(h->fchild,stck,top--,select_number);
        display_help(h->next_brother,stck,top--,select_number);
    }
}
void trie_tree::display_help(node* h,node* stck[],int top) const
{
    if(h)
    {
        stck[top++]=h;
        if(h->is_word)
        {
           // select_number++;
            for(int i=0; i<top; i++)
            {
                cout<<stck[i]->data;
            }
            cout<<":";
            cout<<h->explain<<endl;
        }
        display_help(h->fchild,stck,top--);   //递归回来后top--。
        display_help(h->next_brother,stck,top--);
    }
}
void trie_tree::display() const
{
    node* stck[max_number];
    display_help(head->fchild,stck,0);
}
typename trie_tree::node* trie_tree::child_search(node* f,char key) const
{
    node* temp=f->fchild;
    while(temp&&key>=temp->data)
    {
        if(key==temp->data)
        {
            return temp;
        }
        temp=temp->next_brother;
    }
    return NULL;
}
typename trie_tree::node* trie_tree::insrt_child(node* f,char key)    //在结点f下新建一儿子key，按字母表顺序
{
    node* q=f->fchild;
    node* temp=new node;
    temp->fchild=NULL;
    temp->next_brother=NULL;
    temp->is_word=false;
    temp->data=key;
    if(q)
    {
        if(q->data>key)
        {
            temp->next_brother=q;
            f->fchild=temp;
            return temp;
        }
        while(q->next_brother)
        {
            if(q->data<key&&key<q->next_brother->data)
            {
                temp->next_brother=q->next_brother;
                q->next_brother=temp;
                return temp;
            }
            q=q->next_brother;
        }
        q->next_brother=temp;
    }
    else
    {
        f->fchild=temp;
    }
    return temp;
}
void trie_tree::menu()
{
    cout<<"-------------------------------------------------------------------------------"<<endl;
    cout<<"--                      English-Chinese Dictionary                           --"<<endl;
    cout<<"--                 I.press enter to search another word                      --"<<endl;
    cout<<"--                  II.exit whenever you press the esc                       --"<<endl;
    cout<<"---------------------------word number:"<<num<<"------------------------------------"<<endl;
}
char trie_tree::select()
{
    string s;
    char m;
    menu();
    cout<<"search:";
    while(m=getch())
    {
        if(m==enter||m==esc) return m;
        system("cls");
        menu();
        if(m==backspace)
        {
            if(s.length()<2) return m;
            s=s.substr(0,s.length()-1);
            cout<<"search:"<<s<<endl;
        }
        else
        {
            s+=m;
            cout<<"search:"<<s<<endl;
        }
        select_help(s);
    }
    return 0;
}
void trie_tree::select_help(string s)    //依据已输入的字符，选择可匹配的字符串
{
    node* stck[max_number];
    node* temp=head;
    int top=0,select_number=0;
    for(unsigned i=0; i<s.length(); i++)
    {
        temp=child_search(temp,s[i]);
        if(!temp)
        {
            cout<<"no match."<<endl;
            return;
        }
        stck[top++]=temp;
    }
    if(temp->is_word)
    {
        select_number++;
        for(int i=0; i<top; i++)
            cout<<stck[i]->data;
        cout<<":"<<temp->explain<<endl;
    }
    display_help(temp->fchild,stck,top,select_number);
    cout<<"                                  find "<<select_number<<" word(s)"<<endl;
}
//    node* temp2=child_search(f,m);
//    if(!temp2)
//    {
//        // cout<<"no match."<<endl;
//        return NULL;
//    }
//
//    //temp=temp2;
//    stck[top++]=temp2;
//    cout<<"input:";
//    for(int i=0; i<top; i++)
//    {
//        cout<<stck[i]->data;
//    }
//    cout<<endl;
//    if(temp2->is_word)
//    {
//        for(int i=0; i<top; i++)
//            cout<<stck[i]->data;
//        cout<<":"<<stck[top-1]->explain<<endl;
//    }
//    display_help(temp2->fchild,stck,top);
//    return temp2;
void trie_tree::insrt(string s,string e)
{
    node* temp=head;
    node* temp2;
    for(unsigned i=0; i<s.length(); i++)
    {
        temp2=child_search(temp,s[i]);      //查询是s[i]是不是temp的儿子，若是返回指向该儿子的节点到temp2，若不是temp2为空
        if(temp2)  temp=temp2;
        else  temp=insrt_child(temp,s[i]);    //若temp2为空，则temp新建一个儿子，temp指向该新建的儿子
    }
    if(!temp->is_word) num++;
    temp->is_word=true;
    temp->explain=e;
}
void trie_tree::consult()
{
    char choose=0;
    while(choose!=esc)
    {
        system("cls");
        choose=select();
    }
}
#endif // TRIE_TREE_H_INCLUDED
