#ifndef TRANS_H_INCLUDED
#define TRANS_H_INCLUDED
#include <iostream>
#define maxlength 1000
using namespace std;
template<class T>
class btree
{
private:
    bool exist;
    btree* fchild;
    T data;
    btree* next;
public:
    btree();
    ~btree() {};
    void create();
    void visit() const;
    void preorder() const;

    void inorder() ;
    void postorder() const;
    void makenull();

};
template<class T>
void btree<T>::inorder()
{
    int f=0,l=0,num=1;
    btree<T> *temp,*que[maxlength];
    temp=this;
    while(1)
    {
        if(temp->exist)
        {
            temp->visit();
            if(temp->fchild->exist) que[l++]=temp->fchild;
            if(temp->next->exist)
            {
                temp=temp->next;
            }
            else
            {
                if(f!=l)
                {
                    temp=que[f++];
                }
                else
                {
                    break;
                }
            }
        }

    }
}
template<class T>
void btree<T>::postorder() const
{
    if(exist)
    {
        fchild->postorder();
        visit();
        next->postorder();
    }

}
template<class T>
void btree<T>::preorder() const
{
    if(exist)
    {
        visit();
        fchild->preorder();
        next->preorder();
    }

}
template<class T>
btree<T>::btree()
{
    exist=false;
    data=NULL;
    fchild=nullptr;
    next=nullptr;
}
template<class T>
void btree<T>::visit() const
{
    cout.width(5);
    cout<<data;
}
template<class T>
void btree<T>::makenull()
{
    exist=false;
    data=NULL;
    fchild=nullptr;
    next=nullptr;
}
template<class T>
void btree<T>::create()
{
    T x;
    cin>>x;
    if(x=='.');
    else
    {
        exist=true;
        data=x;
        cout<<x<<"'s first child is('.' represents no child):";
        fchild=new btree<T>;
        fchild->create();
        cout<<x<<"'s next brother is('.' represents no brother):";
        next=new btree<T>;
        next->create();
    }
}
#endif // TRANS_H_INCLUDED
