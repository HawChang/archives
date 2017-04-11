#ifndef avlTree_H_
#define avlTree_H_
#include <iostream>
using namespace std;
template <class T>
class treeNode
{
public:
    treeNode():lchild(NULL),rchild(NULL),height(1),freq(1) {}
    T data;
    treeNode*lchild;
    treeNode*rchild;
    int height;
    unsigned int freq;//frequency
};
template <class T>
class avlTree
{
public:
    //interface
    avlTree():root(NULL) {}
    void insert(T x);
    void deletex(T x);
    void inOrderTraversal();
    void consult(T x);
private:
    treeNode<T> *root;
    void insertHelp(treeNode<T> *&node,T x);
    treeNode<T> *findHelp(treeNode<T>*node,T x);
    void inOrderHelp(const treeNode<T> *node);
    void deleteHelp(treeNode<T> *&node,T x);
    int height(treeNode<T> *node);
    void singRotateLeft(treeNode<T> *&k2);
    void singRotateRight(treeNode<T> *&k2);
    void doubleRotateLR(treeNode<T> *&k3);
    void doubleRotateRL(treeNode<T> *&k3);
    int max(int a,int b);
    treeNode<T> *find(T x);
};
template <class T>
void avlTree<T>::consult(T x)
{
    cout<<"the charactor:"<<find(x)->data;
    if(find(x)->lchild)
        cout<<"  its lchild:"<<find(x)->lchild->data;
    else cout<<"  its lchild: null";
    if(find(x)->rchild)
        cout<<"  its rchild:"<<find(x)->rchild->data;
    else cout<<"  its rchild: null";
    cout<<endl
        <<"  the height:"<<find(x)->height
        <<"  its frequency:"<<find(x)->freq
        <<endl;
}


template <class T>
int avlTree<T>::height(treeNode<T> *node)
{
    if(node!=NULL)
        return node->height;
    else
        return 0;
}
template <class T>
int avlTree<T>::max(int a,int b)
{
    return a>b?a:b;
}
//LL->right
template <class T>
void avlTree<T>::singRotateRight(treeNode<T> *&k2)
{
    treeNode<T> *k1;
    k1=k2->lchild;
    k2->lchild=k1->rchild;
    k1->rchild=k2;

    k2->height=max(height(k2->lchild),height(k2->rchild))+1;
    k1->height=max(height(k1->lchild),k2->height)+1;
    // root=k1;
    treeNode<T>*temp =k2;
    k2=k1;
    k1=temp;
}
//RR->left
template <class T>
void avlTree<T>::singRotateLeft(treeNode<T> *&k2)
{
    treeNode<T> *k1;
    k1=k2->rchild;
    k2->rchild=k1->lchild;
    k1->lchild=k2;

    k2->height=max(height(k2->lchild),height(k2->rchild))+1;
    k1->height=max(height(k1->rchild),k2->height)+1;
    // root=k1;
    treeNode<T>*temp =k2;
    k2=k1;
    k1=temp;
}
//LR->left->right
template <class T>
void avlTree<T>::doubleRotateLR(treeNode<T> *&k3)
{
    singRotateLeft(k3->lchild);
    singRotateRight(k3);
}
//RL->right->left
template <class T>
void avlTree<T>::doubleRotateRL(treeNode<T> *&k3)
{
    singRotateRight(k3->rchild);
    singRotateLeft(k3);
}
template <class T>
void avlTree<T>::insertHelp(treeNode<T> *&node,T x)
{
    if(node==NULL)
    {
        node=new treeNode<T>;
        node->data=x;
        return;
    }
    if(node->data>x)
    {
        insertHelp(node->lchild,x);
        if(height(node->lchild)-height(node->rchild)==2)
        {
            if(x<node->lchild->data)
                singRotateRight(node);
            else
                doubleRotateLR(node);
        }
    }
    else if(node->data<x)
    {
        insertHelp(node->rchild,x);
        //   cout<<height(node->rchild)<<"  "<<height(node->lchild)<<endl;
        if(height(node->rchild)-height(node->lchild)==2)
        {
            if(x>node->rchild->data)
                singRotateLeft(node);
            else
                doubleRotateRL(node);
        }
    }
    else
    {
        ++(node->freq);
        cout<<"already has the charactor."<<endl;
    };
    node->height=max(height(node->lchild),height(node->rchild))+1;
}
template <class T>
void avlTree<T>::insert(T x)
{
    insertHelp(root,x);
}
template <class T>
treeNode<T>* avlTree<T>::findHelp(treeNode<T>*node,T x)
{
    if(node==NULL)return NULL;
    else if(node->data>x)
    {
        return findHelp(node->lchild,x);
    }
    else if(node->data<x)
    {
        return findHelp(node->rchild,x);
    }
    else return node;
}
template <class T>
treeNode<T>*avlTree<T>::find(T x)
{
    return findHelp(root,x);
}
template <class T>
void avlTree<T>::inOrderHelp(const treeNode<T>*node)
{
    if(node==NULL)return;
    inOrderHelp(node->lchild);
    cout<<node->data<<" ";
    inOrderHelp(node->rchild);
}
template <class T>
void avlTree<T>::inOrderTraversal()
{
    inOrderHelp(root);
}
template <class T>
void avlTree<T>::deleteHelp(treeNode<T>*&node,T x)
{
    if(node==NULL)return;
    if(x<node->data)
    {
        deleteHelp(node->lchild,x);
        if(height(node->rchild)-height(node->lchild)==2)
        {
            if(node->rchild->lchild!=NULL &&(height(node->rchild->lchild)>height(node->rchild->rchild)))
                doubleRotateRL(node);
            else
                singRotateLeft(node);
        }
    }
    else if(x>node->data)
    {
        deleteHelp(node->rchild,x);
        if(height(node->lchild)-height(node->rchild)==2)
        {
            if(node->lchild->rchild!=NULL && (height(node->lchild->rchild)>height(node->lchild->lchild)))
                doubleRotateLR(node);
            else
                singRotateRight(node);
        }
    }
    else
    {
        if(node->lchild&&node->rchild)
        {
            treeNode<T>*temp=node->rchild;
            while(temp->lchild!=NULL)
                temp=temp->lchild;//找到右子树中值最小的节点
            node->data=temp->data;
            node->freq=temp->freq;
            deleteHelp(node->rchild,temp->data);
            if(height(node->lchild)-height(node->rchild)==2)
            {
                if(node->lchild->rchild && (height(node->lchild->rchild)>height(node->lchild->lchild)))
                    doubleRotateLR(node);
                else
                    singRotateRight(node);
            }
        }
        else
        {
            treeNode<T>*temp=node;
            if(node->lchild==NULL)
                node=node->rchild;
            else if(node->rchild==NULL)
                node=node->lchild;
            delete(temp);
            temp=NULL;
        }
    }
    if(node==NULL)return;
    node->height=max(height(node->lchild),height(node->rchild))+1;
    return;
}
template <class T>
void avlTree<T>::deletex(T x)
{
    deleteHelp(root,x);
}
#endif
