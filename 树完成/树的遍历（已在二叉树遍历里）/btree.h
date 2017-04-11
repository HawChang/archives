#ifndef BTREE_H_INCLUDED
#define BTREE_H_INCLUDED
#include <iostream>
using namespace std;
template<class T>
class btree
{
private:
    btree* lchild;
    T data;
    btree* rchild;
public:
    btree();
    ~btree();
    void create();
    void visit() const;
    void preorder() const;
    void inorder() const;
    void postorder() const;
    void makenull();
    bool ini ();
    void Print(btree<T>* h,int n);
};
template<class T>
void btree<T>::Print(btree<T>* h, int n)
{

    if(h->data==NULL)
    {
        return;
    }
    Print(h->rchild, n+1);
    for(int i=0; i<n; i++)
    {
        cout<<"    ";
    }
    cout<<h->data;
    if(h->lchild->data==NULL&&h->rchild->data==NULL) ;
    else cout<<" -< " ;
    cout<<endl;

    Print(h->lchild, n+1);
}
template<class T>
void btree<T>::makenull()
{
    data=NULL;
    lchild=NULL;
    rchild=NULL;
}
template<class T>
btree<T>::~btree()
{
    cout<<"delete"<<data<<" success"<<endl;
    delete lchild;
    delete rchild;
}
template<class T>
void btree<T>::postorder() const
{
    if(data!=NULL)
    {
        lchild->postorder();
        rchild->postorder();
        visit();
    }
}
template<class T>
void btree<T>::inorder() const
{
    if(data!=NULL)
    {
        lchild->inorder();
        visit();
        rchild->inorder();
    }
}
template<class T>
void btree<T>::preorder() const
{
    if(data!=NULL)
    {
        visit();
        lchild->preorder();
        rchild->preorder();
    }
}
template<class T>
void btree<T>::create()
{
    T x;
    cin>>x;
    if(x=='.');
    else
    {
        data=x;
        cout<<x<<"'s left child is('.'represents no child):";
        lchild=new btree<T>;
        lchild->create();
        cout<<x<<"'s right child is('.'represents no child):";
        rchild=new btree<T>;
        rchild->create();
    }
}
template<class T>
void btree<T>::visit() const
{
    cout<<" "<<data;
}
template<class T>
btree<T>::btree()
{
    data=NULL;
    lchild=NULL;
    rchild=NULL;
}


#endif // BTREE_H_INCLUDED
