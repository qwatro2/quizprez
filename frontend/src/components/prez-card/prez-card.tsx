import { Card, CardContent, Typography } from "@mui/material";
import { styled } from "@mui/material/styles";
import React, { useRef, useEffect, useState } from "react";

const StyledCard = styled(Card)({
    width: 430,
    height: 300,
    overflow: "hidden",
    borderRadius: 10,
    border: "1px solid rgba(27, 44, 43, 0.5)",
    position: "relative",
    margin: "0 auto",
    "&:hover": {
        "transform": "scale(1.05)"
    },
    cursor: "pointer"
});

const SlidePreview = styled("div")({
    width: "100%",
    height: 240,
    overflow: "hidden",
    position: "relative",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#f0f0f0",
});

interface PrezCardProps {
    title: string;
    htmlContent: string;
    onCardClicK: (id: number) => void;
}

export const PrezCard: React.FC<PrezCardProps> = ({ title, htmlContent, onCardClicK }) => {
    const previewRef = useRef<HTMLDivElement>(null);
    const [scale, setScale] = useState(1);

    const getFirstSlideHtml = (html: string): string => {
        const parser = new DOMParser();
        const doc = parser.parseFromString(html, "text/html");
        const firstPageDiv = doc.querySelector('div[id^="page"]');
        return firstPageDiv?.outerHTML || "";
    };

    useEffect(() => {
        if (previewRef.current && htmlContent) {
            const firstSlideHtml = getFirstSlideHtml(htmlContent);
            previewRef.current.innerHTML = firstSlideHtml;

            const content = previewRef.current.firstChild as HTMLElement;
            if (content) {
                // Ждем загрузки изображений (если есть) перед расчетом размеров
                const images = content.getElementsByTagName('img');
                if (images.length > 0) {
                    let loadedImages = 0;
                    const onImageLoad = () => {
                        loadedImages++;
                        if (loadedImages === images.length) {
                            calculateScale(content);
                        }
                    };

                    Array.from(images).forEach(img => {
                        if (img.complete) {
                            loadedImages++;
                        } else {
                            img.addEventListener('load', onImageLoad);
                        }
                    });

                    if (loadedImages === images.length) {
                        calculateScale(content);
                    }
                } else {
                    calculateScale(content);
                }
            }
        }
    }, [htmlContent]);

    const calculateScale = (content: HTMLElement) => {
        // Получаем реальные размеры содержимого слайда
        const contentRect = content.getBoundingClientRect();
        const contentWidth = contentRect.width;
        const contentHeight = contentRect.height;

        // Размеры области предпросмотра
        const previewWidth = 430;
        const previewHeight = 400;

        // Вычисляем масштаб для ширины и высоты
        const widthScale = previewWidth / contentWidth;
        const heightScale = previewHeight / contentHeight;

        // Используем минимальный масштаб, чтобы весь слайд поместился
        const finalScale = Math.min(widthScale, heightScale);

        setScale(finalScale);

        // Применяем стили
        content.style.transform = `scale(${finalScale})`;
        content.style.transformOrigin = "top left";
        content.style.position = "absolute";
        content.style.top = "0";
        content.style.left = "0";
    };

    return (
        <StyledCard onClick={onCardClicK}>
            <SlidePreview ref={previewRef} />
            <CardContent sx={{ px: 4, pt: 1.5 }}>
                <Typography
                    variant="h2"
                    sx={{
                        fontWeight: "normal",
                        fontSize: "1.5rem",
                        color: "black",
                        fontFamily: "'Inter-Regular', Helvetica, Arial, sans-serif",
                        whiteSpace: "nowrap",
                        overflow: "hidden",
                        textOverflow: "ellipsis",
                    }}
                >
                    {title}
                </Typography>
            </CardContent>
        </StyledCard>
    );
};

export default PrezCard;