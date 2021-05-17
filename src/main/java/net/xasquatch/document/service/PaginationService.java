package net.xasquatch.document.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaginationService {

    private int[] calculationToPage(int pageLimit, int currentPageBlock, int totalCount) {

        int totalPageBlock = (totalCount / pageLimit);
        int startPageBlock = 1;
        int endPageBlock = 5;
        if (totalCount % pageLimit > 0)
            totalPageBlock += 1;

        if (currentPageBlock % 5 == 1) {
            startPageBlock = currentPageBlock;

        } else if (currentPageBlock % 5 == 0) {
            startPageBlock = currentPageBlock - 4;

        } else {
            startPageBlock = ((currentPageBlock / 5) * 5) + 1;

        }

        endPageBlock = startPageBlock + 4; // 4 = 5-1
        if (endPageBlock > totalPageBlock)
            endPageBlock = totalPageBlock;

        int prevPageBlock = startPageBlock - 1;

        int nextPageBlock = endPageBlock + 1;
        if (nextPageBlock > totalPageBlock)
            nextPageBlock = totalPageBlock;

        return new int[]{prevPageBlock, nextPageBlock, endPageBlock};
    }


    public List<String> getPageBlockList(int pageLimit, int currentPageBlock, int totalCount) {

        int[] pageBlocks = calculationToPage(pageLimit, currentPageBlock, totalCount);
        int prevPageBlock = pageBlocks[0];
        int nextPageBlock = pageBlocks[1];
        int endPageBlock = pageBlocks[2];

        List<String> blockList = new ArrayList<String>();
        for (int i = prevPageBlock; i <= nextPageBlock; i++) {
            if (i != 0) {
                if (i == currentPageBlock) {
                    blockList.add("current-page");

                } else {
                    blockList.add(String.valueOf(i));

                }
            }
        }
        return blockList;
    }
}
