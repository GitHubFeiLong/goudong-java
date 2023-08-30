import defaultSettings from '@/settings'

const title = defaultSettings.title || 'Vue Element Admin'

export default function getPageTitle(route) {
  if (route.meta && route.meta.title) {
    return `${route.meta.title} - ${title}`
  }
  return `${title}`
}
