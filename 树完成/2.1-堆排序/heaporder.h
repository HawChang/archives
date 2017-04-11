#ifndef HEAPORDER_H_INCLUDED
#define HEAPORDER_H_INCLUDED
#define maxsize  200
using namespace std;
template<class T>
class heap
{
public:
    T  element[maxsize];
    int n;
    heap()
    {
        n=0;
    }
    bool heapempty(heap<T> *h);
    bool heapfull(heap<T> *h);
    bool insrt(heap<T> *h,T x);
    T maxout(heap<T> *h);
};
template<class T>
bool heap<T>::heapempty(heap<T> *h)
{
    return (!h->n);
}
template<class T>
bool heap<T>::heapfull(heap<T> *h)
{
    if(h->n==maxsize-1) true;
    else return false;
}
template<class T>
bool heap<T>::insrt(heap<T> *h,T x)
{
    int i;
    if(!h->heapfull(h))
    {
        i=++h->n;
        while((i!=1)&&x>h->element[i/2])
        {
            h->element[i]=h->element[i/2];
            i/=2;
        }
        h->element[i]=x;
        return true;
    }
    return false;
}
template <class T>
T heap<T>::maxout(heap<T> *h)
{
    int parent=1,child=2;
    T element,temp;
    if(!h->heapempty(h))
    {
        element=h->element[1];
        temp=h->element[h->n--];
        while(child<=h->n)
        {
            if((child<h->n)&&(h->element[child]<h->element[child+1])) child++;
            if(temp>=h->element[child]) break;
            h->element[parent]=h->element[child];
            parent=child;
            child*=2;
        }
        h->element[parent]=temp;
        return element;
    }
}


#endif // HEAPORDER_H_INCLUDED
