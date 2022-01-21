template <typename T>
class MyClass
{
  public:
    MyClass(T data);
    T getData();

  private:
    T m_data;
};
template <typename T>
MyClass<T>::MyClass(T data) :
    m_data(data)
{
}
template <typename T>
T MyClass<T>::getData()
{
    return m_data;
}