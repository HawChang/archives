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
    btree* lchild;
    T data;
    btree* rchild;
public:
    btree();
    ~btree() {};
    void create();
    void visit() const;
    void preorder() const;
    void inorder() ;
    void postorder() const;
    void makenull();
    void nonre_inorder();
    void nonre_preorder();
    void nonre_postorder();
};
template<class T>
void btree<T>::nonre_postorder()
{
    int f=0;
    btree<T> *temp,*temp2,*stck[maxlength];
    temp=this;
    while(f!=-1)
    {
        while(temp)
        {
            stck[f++]=temp;
            temp=temp->lchild;
        }
        temp=stck[f-1];
        if(temp->rchild==NULL||temp->rchild==temp2)
        {
            if(temp->rchild!=NULL)
            {
                temp->visit();
            }
            temp2=temp;
            temp=NULL;
            f--;
        }
        else temp=temp->rchild;

    }
}
template<class T>
void btree<T>::nonre_inorder()
{
    int f=0;
    btree<T> *temp,*stck[maxlength];
    temp=this;
    while(f!=-1)
    {
        if(temp->exist)
        {
            stck[f++]=temp;
            temp=temp->lchild;
        }
        else
        {
            temp=stck[--f];
            if(f!=-1) {
                    temp->visit();
            temp=temp->rchild;
            }
        }
    }
}
template<class T>
void btree<T>::nonre_preorder()
{
    int f=0;
    btree<T> *temp,*stck[maxlength]= {NULL};
    temp=this;
    while(f!=-1)
    {
        if(temp->exist)
        {
            temp->visit();
            if(temp->rchild->exist) stck[f++]=temp->rchild;
            if(temp->lchild->exist)
            {
                temp=temp->lchild;
            }
            else
            {

                temp=stck[--f];
            }
        }

    }
}
template<class T>
void btree<T>::inorder()
{
    if(exist)
    {
        lchild->inorder();
        visit();
        rchild->inorder();
    }
}
template<class T>
void btree<T>::postorder() const
{
    if(exist)
    {
        lchild->postorder();
        rchild->postorder();
        visit();
    }
}
template<class T>
void btree<T>::preorder() const
{
    if(exist)
    {
        visit();
        lchild->preorder();
        rchild->preorder();
    }
}
template<class T>
btree<T>::btree()
{
    exist=false;
    data=NULL;
    lchild=NULL;
    rchild=NULL;
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
    lchild=NULL;
    rchild=NULL;
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
        cout<<x<<"'s lchild is('.' represents no child):";
        lchild=new btree<T>;
        lchild->create();
        cout<<x<<"'s rchild is('.' represents no brother):";
        rchild=new btree<T>;
        rchild->create();
    }
}
#endif // TRANS_H_INCLUDED
