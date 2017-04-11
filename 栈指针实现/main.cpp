#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#define maxlength 1000
#define maxl 100
using namespace std;
template<class T>
class node
{
public:
    T val;
    node<T> *next;
};
template<class T>
class stck
{
public:
    node<T> *head;
    void makenull();
    void push(T x);
    void pop();
    T top();
    bool emptystck();
};
template<class T>
void stck<T>::makenull()
{
    head->next=NULL;
}
template <class T>
void stck<T>::push(T x)
{
    node<T> *temp=new node<T>;
    temp->val=x;
    temp->next=head->next;
    head->next=temp;
}
template <class T>
void stck<T>::pop()
{
    if(!emptystck())
    {
        node<T> *temp=new node<T>;
        temp=head->next;
        head->next=temp->next;
        delete temp;
    }
    else
    {
        cout<<"stack is empty."<<endl;
        exit(1);
    }
}
template<class T>
T stck<T>::top()
{
    if(!emptystck())
        return (head->next->val);
    else ;
     return NULL;
}
template<class T>
bool stck<T>::emptystck()
{
    if(head->next)
        return false;
    else
        return true;
}
int main()
{
    return 0;
}
