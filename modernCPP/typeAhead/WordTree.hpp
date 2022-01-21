#pragma once

#include <cstdint>
#include <memory>
#include <string>
#include <unordered_map>
#include <vector>

class TreeNode
{
  public:
    bool endOfWord;
    std::unordered_map<char, std::shared_ptr<TreeNode>> children;

    TreeNode(bool end = false)
    {
        endOfWord = end;
    }
};

class WordTree
{
  public:
    void add(std::string word);
    bool find(std::string word);

    std::vector<std::string> predict(std::string partial, std::uint8_t howMany);
    std::size_t size();

    WordTree()
    {
        m_root = std::make_shared<TreeNode>();
        m_size = 0;
    }

  private:
    std::shared_ptr<TreeNode> m_root;
    std::uint16_t m_size;

    void add(std::string word, std::size_t currIdx, std::shared_ptr<TreeNode> node);
    bool find(std::string word, std::size_t currIdx, std::shared_ptr<TreeNode> node);

    std::shared_ptr<TreeNode> findPartialRoot(std::string partial);
};